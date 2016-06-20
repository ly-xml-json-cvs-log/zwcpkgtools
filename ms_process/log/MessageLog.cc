#include "MessageLog.hh"

RWMutexLock::RWMutexLock()
{
    pthread_mutex_init(&m_mutex, NULL);
}

RWMutexLock::~RWMutexLock()
{
    pthread_mutex_destroy(&m_mutex);
}

void RWMutexLock::acquire()
{
    pthread_mutex_lock(&m_mutex);
}

void RWMutexLock::release()
{
    pthread_mutex_unlock(&m_mutex);
}

MessageLog::Record::Object::Global MessageLog::Record::Object::global;

///////////////////////////////////////////////////////////////////////////////
// MessageLog::Record implementation

MessageLog::Record::Record(MessageLog &log) : object(Object::get(log.advise))
{
}

MessageLog::Record::Record(const Record &rhs) : object(rhs.object)
{
    if (object)
        object->ref();
}

MessageLog::Record::~Record()
{
    if (object)
        object->unref();
}

MessageLog::Record &MessageLog::Record::operator=(const MessageLog::Record &rhs)
{
    if (rhs.object)
        rhs.object->ref();
    if (object)
        object->unref();
    object = rhs.object;
    return *this;
}

std::ostream &MessageLog::Record::stream()
{
    if (object)
        return object->stream();
    return std::cout;
}

///////////////////////////////////////////////////////////////////////////////
// MessageLog::Record::Object implementation

MessageLog::Record::Object::Object() :
    next(0), useRef(1), advise(0), ostream(this)
{
    setp(buf, buf + sizeof(buf) - 1);
}

MessageLog::Record::Object::~Object()
{   
}

MessageLog::Record::Object *MessageLog::Record::Object::get(MessageLog::Advise *advise)
{
    Object *object = global.get();
    if (object)
        object->advise = advise;
    return object;
}

void MessageLog::Record::Object::ref()
{
    ++ useRef;
}

void MessageLog::Record::Object::unref()
{
    if (-- useRef == 0)
       release();
}

int MessageLog::Record::Object::overflow(int c)
{
    *pptr() = '\0';
    advise->logMessage(pbase());
    
    int nbytes = pptr() - pbase();
    pbump(-nbytes);

    if (c == EOF)
        return EOF;

    *pptr() = c;
    pbump(1);

    return 0;
}

int MessageLog::Record::Object::sync()
{
    return 0;
}

void MessageLog::Record::Object::release()
{
    int nbytes = pptr() - pbase();
    if (nbytes > 0)
    {
        *pptr() = '\0';
        advise->logMessage(pbase());
        pbump(-nbytes);
    }

    advise = 0;
    global.put(this);
}

std::ostream &MessageLog::Record::Object::stream()
{
    return ostream;
}

///////////////////////////////////////////////////////////////////////////////
// MessageLog::Global implementation
MessageLog::Record::Object::Global::Global() : cache(0)
{
}

MessageLog::Record::Object::Global::~Global()
{
    Record::Object *object;
    while (cache)
    {
        object = cache;
        cache = object->next;
        delete object;
    }
}

MessageLog::Record::Object *MessageLog::Record::Object::Global::get()
{
    Record::Object *object;

    lock.acquire();
    if (cache)
    {
        object = cache;
        cache = object->next;
        lock.release();
        ++ object->useRef;
    }
    else
    {
        lock.release();
        try
        {
            object = new Record::Object;
        }
        catch( ... )
        {
            object = 0;
        }
    }

    return object;
}

void MessageLog::Record::Object::Global::put(MessageLog::Record::Object *object)
{
    lock.acquire();
    object->next = cache;
    cache = object;
    lock.release();
}

///////////////////////////////////////////////////////////////////////////////
// MessageLog implementation

MessageLog::MessageLog(MessageLog::Advise *advise) :
    severity(WARN), advise(advise)
{
}

MessageLog::MessageLog(std::ostream &ostream) :
    severity(WARN), advise(new StreamAdvise(ostream))
{
}

MessageLog::~MessageLog()
{
    delete advise;
}

int MessageLog::getSeverity() const
{
    return severity;
}

void MessageLog::setSeverity(int severity)
{
    this->severity = severity;
}

MessageLog::Record MessageLog::record()
{
    return Record(*this);
}
