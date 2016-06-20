#ifndef MESSAGE_LOG_HH
#define MESSAGE_LOG_HH

#include <unistd.h>
#include <sys/types.h>
#include <iostream>
#include <iomanip>
#include <sstream>
#include <pthread.h>
#if defined(linux) || defined(__linux)
#include <sys/syscall.h>
#endif

class RWMutexLock
{
public:
    RWMutexLock();
    ~RWMutexLock();
    void acquire();
    void release();
    
private:
    mutable pthread_mutex_t m_mutex;
};

// @class MessageLog - message log
class MessageLog
{
public:
    enum
    {
        FATAL = 0,
        ERROR = 1,
        WARN = 2,
        INFO = 3,
        DEBUG = 4
    };

    // @class Advise - the interface that use by MessageLog to write one message
    class Advise
    {
    public:
        virtual ~Advise() {}

        // logMessage - log one message
        // @message: pointer to the message string to be log
        virtual void logMessage(const char *message) = 0;
    };

    // @class StreamAdvise - log to iostream
    class StreamAdvise : public Advise
    {
    public:
        StreamAdvise(std::ostream &ostream) : ostream(ostream)
        {
        }

        void logMessage(const char *message)
        {
            ostream << message;
            ostream.flush();
        }

    private:
        std::ostream &ostream;
    };

    // @class Record - class use to record one log message
    class Record
    {
    public:
        Record(MessageLog &log);
        Record(const Record &rhs);
        ~Record();

        Record &operator=(const Record &rhs);
        std::ostream &stream();

    private:
        class Object : private std::streambuf
        {
        public:
            Object();
            ~Object();

            static Object *get(Advise *advise);

            void ref();
            void unref();

            std::ostream &stream();

        private:
            int overflow(int c);
            int sync();
            void release();

            Object *next;
            int useRef;
            Advise *advise;
            std::iostream ostream;
            char buf[4096];

            class Global
            {
            public:
                Global();
                ~Global();

                Object *get();
                void put(Object *object);

            private:
                RWMutexLock lock;
                Object *cache;
            };

            static Global global;
        };

        Object *object;
    };

    MessageLog(Advise *advise);
    MessageLog(std::ostream &ostream);
    ~MessageLog();

    int getSeverity() const;
    void setSeverity(int severity);

    Record record();

private:
    MessageLog(const MessageLog &rhs);
    MessageLog &operator=(const MessageLog &rhs);

    int severity;
    Advise *advise;
};

// msForMessageLog - template function use to generate msec context for message log
template<int line>
static bool msForMessageLog(unsigned int ms)
{
    static struct timespec ts = { 0, 0 };

    struct timespec clock;
    clock_gettime(CLOCK_MONOTONIC, &clock);

    unsigned int elapsed = (clock.tv_sec - ts.tv_sec) * 1000 + (clock.tv_nsec - ts.tv_nsec) / 1000000;
    if (elapsed < ms)
        return false;

    ts = clock;         // Update timestamp
    return true;
}

#define MESSAGE_LOG(log, severity) if (log.getSeverity() >= MessageLog::severity) log.record().stream()
#define MESSAGE_LOG_MSEC(log, severity, milliseconds)  if ((log.getSeverity() >= MessageLog::severity) && msForMessageLog<__LINE__>(milliseconds)) log.record().stream()

#if defined(linux) || defined(__linux__)

// in Linux system, the way to get thread id is to use gettid system call
#define MESSAGE_LOG_PREFIX(log, severity) MESSAGE_LOG(log, severity) << "#" << std::setw(6) << (int)getpid() << "-" << std::setw(3) << (int)syscall(SYS_gettid)  << std::setw(0) << ": "

#else

// in some unix system such as solaris, the way to get thread id is to use pthread_self system call
#define MESSAGE_LOG_PREFIX(log, severity) MESSAGE_LOG(log, severity) << "#" << std::setw(6) << (int)getpid() << "-" << std::setw(3) << (int)pthread_self()  << std::setw(0) << ": "

#endif

#endif
