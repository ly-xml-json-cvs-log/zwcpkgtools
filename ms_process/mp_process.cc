#include <stdlib.h>
#include <stdio.h>
#include <signal.h>
#include <unistd.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <string>
#include <iostream>
#include <fstream>
#include "log/MessageLog.hh"
#include "daemonize.hh"

int count;  //子进程创建成功数量 
int fcount; //子进程创建失败数量 
int scount; //子进程回收数量

MessageLog g_log(std::cout);
#define IOM_LOG(severity)  MESSAGE_LOG(g_log, severity) << "[IOM] [pid = " << getpid() << "][" << #severity << "] - "

/*信号处理函数–子进程关闭收集*/
void sig_chld(int signo)
{
    pid_t chldpid;//子进程id
    int stat;//子进程的终止状态
    
    //子进程回收，避免出现僵尸进程
    while((chldpid = wait(&stat)>0))
    {
        scount++;
    }
}

void start_new_process()
{
    while(true)
    {
        IOM_LOG(WARN) << "eeeeeeeeeeeeeeeeeee: " << std::endl;
	usleep(100);
    }
}

class SharedCoutPtr
{
public:
    SharedCoutPtr(std::streambuf* xstream) : mXstream(xstream)
    {
        
    }

    ~SharedCoutPtr()
    {
        std::cout.rdbuf(mXstream);
    } 

private:
    std::streambuf* mXstream;
};

int main()
{
    g_log.setSeverity(5);
    signal(SIGCHLD,sig_chld);
    MessageLog log(std::cout);

    // 创建daemon守护进程
    if (daemon(0, 0)) 
    {
        perror("daemon");
        return -1;
    }

    std::ofstream file("/tmp/std_cout_rdbuf.txt");
    SharedCoutPtr xcout(std::cout.rdbuf(file.rdbuf()));

    IOM_LOG(INFO) << "demon pid = " << getpid() << std::endl;
    for(int i=0;i<10;i++)
    {
        pid_t pid=fork();

	if (pid == -1) {
            IOM_LOG(ERROR) << "fork error" << std::endl;
            break;
        } else if (pid == 0) {
            // 子进程
            IOM_LOG(ERROR) << "ppid= " << getppid() << std::endl;
            start_new_process();
            break;
        } else {
            // 父进程
            IOM_LOG(INFO) << "sucess fork(), ppid= " << getppid() << std::endl;
        }
    }

    do{
        usleep(1000);
    }while(1);

    return 0;
}
