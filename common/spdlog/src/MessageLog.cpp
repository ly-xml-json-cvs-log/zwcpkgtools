#include "MessageLog.h"
#include <iostream>
#include <stdexcept>

using namespace util::log;


std::string MessageLog::MSG_LOGGER = "MessageLog.msg";
std::string MessageLog::ERR_LOGGER = "MessageLog.err";

using stderr_sink = spdlog::sinks::stderr_sink_mt;

static std::string join_msg(std::string& file_name)
{
    return (file_name + ".msg");
}

static std::string join_err(std::string& file_name)
{
    return (file_name + ".err");
}

static long mb_to_bytes(size_t mb)
{
    return (MessageLog::UNIT_MB * mb);
}


MessageLog::MessageLog():singleMsgFile(true)
{
    spdlog::drop_all();
}

MessageLog::~MessageLog()
{
    spdlog::drop_all();
}

void MessageLog::init_single_rotating_logger(std::string fileName, std::string loggerName, size_t max_mb, size_t max_files, bool force_flush)
{
    instance().logger_attribute_maps[loggerName].update(join_msg(fileName), mb_to_bytes(max_mb), max_files,  force_flush);
    instance().msgLogger = spdlog::rotating_logger_mt(loggerName, join_msg(fileName), mb_to_bytes(max_mb), max_files);
    instance().global_level(MessageLog::trace);
}

void MessageLog::init_rotating_logger(std::string fileName, std::string loggerName, size_t max_mb, size_t max_files, bool force_flush)
{
    instance().singleMsgFile = false;
    std::string msg_logger = MessageLog::MSG_LOGGER;
    std::string err_logger = MessageLog::ERR_LOGGER;

    if (loggerName=="" || loggerName.empty())
    {
        instance().logger_attribute_maps[msg_logger].update(fileName, mb_to_bytes(max_mb), max_files, force_flush);
        instance().logger_attribute_maps[err_logger].update(fileName, mb_to_bytes(max_mb), max_files, force_flush);
    }
    else
    {
        msg_logger = join_msg(loggerName);
        err_logger = join_err(loggerName);

        instance().logger_attribute_maps[msg_logger].update(fileName, mb_to_bytes(max_mb), max_files, force_flush);
        instance().logger_attribute_maps[err_logger].update(fileName, mb_to_bytes(max_mb), max_files, force_flush);
    }

    instance().msgLogger = spdlog::rotating_logger_mt(msg_logger, fileName, mb_to_bytes(max_mb), max_files, force_flush, "msg");
    instance().errLogger = spdlog::rotating_logger_mt(err_logger, fileName, mb_to_bytes(max_mb), max_files, force_flush, "err");
    instance().cerrLogger = spdlog::stderr_logger_mt("console");

    instance().global_level(MessageLog::trace);
}

void MessageLog::set_ConsoleLevel(MessageLog::LogLevel level)
{
    instance().cerrLogger->set_level((SPDLogLevel)level);
}

std::shared_ptr<spdlog::logger> MessageLog::getLogger(LogLevel _level)
{
    switch (_level)
    {
    case MessageLog::fatal:;
    case MessageLog::error:;
    case MessageLog::warn:
        return (singleMsgFile ? msgLogger : errLogger);
    case MessageLog::notice:;
    case MessageLog::info:;
    case MessageLog::debug:;
    case MessageLog::trace:
        return msgLogger;
    default:
        std::cerr << "[WARNING] - ####### Default using message level log!" << std::endl;
        return msgLogger;
    }
}

spdlog::details::line_logger MessageLog::get(LogLevel _level)
{
    switch (_level)
    {
    case MessageLog::fatal:
        return MessageLog::getLogger(_level)->critical();
    case MessageLog::error:
        return MessageLog::getLogger(_level)->error();
    case MessageLog::warn:
        return MessageLog::getLogger(_level)->warn();
    case MessageLog::notice:
        return MessageLog::getLogger(_level)->notice();
    case MessageLog::info:
        return MessageLog::getLogger(_level)->info();
    case MessageLog::debug:
        return MessageLog::getLogger(_level)->debug();
    case MessageLog::trace:
        return MessageLog::getLogger(_level)->trace();
    default:
        return MessageLog::getLogger(_level)->info();
    }
}
