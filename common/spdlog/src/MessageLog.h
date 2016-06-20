#pragma once

#include "../include/spdlog.h"
#include <memory>
#include <map>

using SPDLogLevel = spdlog::level::level_enum;


namespace util
{

namespace log
{

struct MessageLogAttribute
{
    bool force_flush;
    size_t max_files;
    long max_file_size;
    std::string file_name;

    void update(std::string fileName, size_t max_file_size, size_t max_files, bool force_flush)
    {
        file_name     = fileName;
        max_file_size = max_file_size;
        max_files     = max_files;
        force_flush   = force_flush;
    }

    MessageLogAttribute():force_flush(false),max_files(7),max_file_size(1048576 * 100), file_name("./message")
    {
    }
};

class MessageLog
{
public:
    enum LogLevel
    {
        trace  = 0,
        debug  = 1,
        info   = 2,
        notice = 3,
        warn   = 4,
        error  = 5,
        fatal  = 6  // 对应Fatal级别
    };

    static const size_t UNIT_MB = 1048576;
    static std::string MSG_LOGGER;
    static std::string ERR_LOGGER;

    /************************************************************************/
    /* 默认包括函msg跟err两个后缀的日志文件                                 */
    /************************************************************************/


    static MessageLog& instance()
    {
        static MessageLog msgLog;
        return msgLog;
    }

    /************************************************************************/
    /*  单文件log输出                                                       */
    /*      fileName   代表输出文件的路径名称， 程序会自动加上log           */
    /*      loggerName 代表输出的Logger对象                                 */
    /*      max_mb     单个文件最大的MB                                     */
    /*      max_files  最大文件循环                                         */
    /************************************************************************/
    static void init_single_rotating_logger(std::string fileName, std::string loggerName, size_t max_mb = 1024, size_t max_files = 7, bool force_flush = false);

    /************************************************************************/
    /* err 跟 msg后缀的log输出                                              */
    /*      fileName   代表输出文件的路径名称， 程序会自动加上log           */
    /*      loggerName 代表输出的Logger对象                                 */
    /*      max_mb     单个文件最大的MB                                     */
    /*      max_files  最大文件循环                                         */
    /************************************************************************/
    static void init_rotating_logger(std::string fileName, std::string loggerName, size_t max_mb = 1024, size_t max_files = 7, bool force_flush = false);

    void set_MsgLevel(MessageLog::LogLevel level)
    {
        if (msgLogger.get())
            msgLogger->set_level((SPDLogLevel)level);
    }

    void set_ErrLevel(MessageLog::LogLevel level)
    {
        if (errLogger.get())
            errLogger->set_level((SPDLogLevel)level);
    }

    void set_ConsoleLevel(MessageLog::LogLevel level);

    spdlog::details::line_logger get(MessageLog::LogLevel _level);
    inline spdlog::logger& cerr() {return (*cerrLogger);}

    // inline
    inline void global_level(MessageLog::LogLevel level = MessageLog::info) { spdlog::set_level((SPDLogLevel)level); }
    static inline std::shared_ptr<spdlog::logger> get(std::string loggerName) {return spdlog::get(loggerName);}

    MessageLogAttribute& defaultMsgAttri(){ return logger_attribute_maps[MSG_LOGGER];}
    MessageLogAttribute& defaultErrAttri(){ return  (singleMsgFile ? logger_attribute_maps[MSG_LOGGER] : logger_attribute_maps[ERR_LOGGER]);}

private:
    MessageLog();
    ~MessageLog();
    MessageLog& operator=(const MessageLog&);

    std::shared_ptr<spdlog::logger> MsgLogger() {return msgLogger;}
    std::shared_ptr<spdlog::logger> ErrLogger() {return (singleMsgFile ? msgLogger : errLogger);}
    std::shared_ptr<spdlog::logger> getLogger(LogLevel level);

    bool singleMsgFile;
    std::shared_ptr<spdlog::logger> msgLogger;
    std::shared_ptr<spdlog::logger> errLogger;
    std::shared_ptr<spdlog::logger> cerrLogger;
    std::map<std::string, MessageLogAttribute> logger_attribute_maps;
};

}

}

#define FUNCTION_TRACE "[" << __FILE__ << ":"<< __LINE__ << "]-"
#define MESSGE_LOG(_level)  MessageLog::instance().get(MessageLog::_level)

#define MSG_LOG_CERR()  MessageLog::instance().cerr().trace() << FUNCTION_TRACE

#define MSG_LOG_TRACE()   MESSGE_LOG(trace)  << FUNCTION_TRACE
#define MSG_LOG_DEBUG()   MESSGE_LOG(debug)  << FUNCTION_TRACE
#define MSG_LOG_INFO()    MESSGE_LOG(info)   << FUNCTION_TRACE
#define MSG_LOG_NOTICE()  MESSGE_LOG(notice) << FUNCTION_TRACE
#define MSG_LOG_WARNING() MESSGE_LOG(warn)   << FUNCTION_TRACE
#define MSG_LOG_ERROR()   MESSGE_LOG(error)  << FUNCTION_TRACE
#define MSG_LOG_FATAL()   MESSGE_LOG(fatal)  << FUNCTION_TRACE
