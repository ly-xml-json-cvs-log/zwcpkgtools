#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>

int daemon( int nochdir, int noclose )
{
    pid_t pid;
    if ( !nochdir && chdir("/") != 0 )
        return -1;
  
    if ( !noclose )
    {
        int fd = open("/dev/null", O_RDWR);

        if ( fd < 0 )
            return -1;

        if ( dup2( fd, 0 ) < 0
             || dup2( fd, 1 ) < 0
             || dup2( fd, 2 ) < 0 )
        {
            close(fd);
            return -1;
        }

        close(fd);
    }
    
    pid = fork();
    if (pid < 0)
        return -1;

    if (pid > 0)
        _exit(0);

    if ( setsid() < 0 )
        return -1;

    return 0;
}
