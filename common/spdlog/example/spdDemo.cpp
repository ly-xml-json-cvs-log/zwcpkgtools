#include "MessageLog.h"

using namespace util::log;

int main()
{
    MessageLog::init_rotating_logger("spdlog", "spd-demo");
    MessageLog::instance().set_ConsoleLevel((MessageLog::LogLevel)0);

    long _index = 10000000;
    while(_index --){
        MSG_LOG_FATAL() << "EEEEEEEEEEE" << "-" << "##############";
        MSG_LOG_ERROR() << "EEEEEEEEEEE" << "-" << "##############";
        MSG_LOG_WARNING() << "EEEEEEEEEEE" << "-" << "##############";
        MSG_LOG_NOTICE() << "EEEEEEEEEEE" << "-" << "##############";
        MSG_LOG_INFO() << "EEEEEEEEEEE" << "-" << "##############";
        MSG_LOG_DEBUG() << "EEEEEEEEEEE" << "-" << "##############";
        MSG_LOG_TRACE() << "EEEEEEEEEEE" << "-" << "##############";

        usleep(100);
    }
    return 0;
}