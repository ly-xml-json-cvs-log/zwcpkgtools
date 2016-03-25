#include <sys/types.h>
#include <sys/time.h>
#include <sys/queue.h>
#include <stdlib.h>
#include <err.h>
#include <event.h>
#include <evhttp.h>
#include <unistd.h>
#include <stdio.h>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <time.h>
#include <pthread.h>
 
#define BUFSIZE  4096
#define SLEEP_MS 10
 
char buf[BUFSIZE];
 
int bytes_recvd = 0;
int chunks_recvd = 0;
int closed = 0;
int connected = 0;
 
//static char ip_array[300] =  "192.168.190.134,192.168.190.143,192.168.190.144,192.168.190.145,192.168.190.146,192.168.190.147,192.168.190.148,192.168.190.149,192.168.190.151,192.168.190.152";
static char ip_array[300] =   "192.168.1.200,192.168.1.201,192.168.1.202,192.168.1.203,192.168.1.204,192.168.1.205,192.168.1.206,192.168.1.207,192.168.1.208";
static char server_ip[16] =   "192.168.1.125";
static int server_port = 11798;
static int max_conns = 60000;
 
// called per chunk received
void chunkcb(struct evhttp_request *req, void *arg)
{
    int s = evbuffer_remove( req->input_buffer, &buf, BUFSIZE );
    bytes_recvd += s;
    chunks_recvd++;

    if (connected >= max_conns && chunks_recvd % 10000 == 0)
        printf(">Chunks: %d\tBytes: %d\tClosed: %d\n", chunks_recvd, bytes_recvd, closed);
}
 
// 连接请求完成以后做计数使用, 所有连接使用长连接
void reqcb(struct evhttp_request *req, void *arg)
{
    closed++; 
}
 
/*
 * client 端监听程序, 通过配置本地虚拟ip可以模拟百万级的client socket请求
 */
int main(int argc, char **argv)
{
    int ch;
    while ((ch = getopt(argc, argv, "o:h:p:m:")) != -1) { 
        switch (ch) {
        case 'h':
            printf("host is %s\n", optarg);
            strncpy(server_ip, optarg, 15);
            break;
        case 'p':
            printf("port is %s\n", optarg);
            server_port = atoi(optarg);
            /*strncpy(server_ip, optarg, 15);*/
            break;
        case 'm':
            printf("max_conns is %s\n", optarg);
            max_conns = atoi(optarg);
            /*strncpy(server_ip, optarg, 15);*/
            break;
        case 'o':
            printf("ori_ips is %s\n", optarg);
            strncpy(ip_array, optarg, 300 - 1);
            break; 
        } 
    }

    event_init();
    struct evhttp *evhttp_connection = NULL;
    struct evhttp_request *evhttp_request;
    char path[32];
    int i;

    char delims[] = ",";
    char *ori_ip = NULL;
    ori_ip = strtok( ip_array, delims );
    int local_port = 3000;
    while (ori_ip != NULL) {
        printf("************ New IP used *************************\n\n");
        for (i = 1; i <= max_conns; i++) {
            evhttp_connection = evhttp_connection_new(server_ip, server_port);
            if (evhttp_connection == NULL)
                printf("evhttp_connection_new failed...\n\n");

            evhttp_connection_set_local_address(evhttp_connection, ori_ip);
            // evhttp_connection_set_local_port(evhttp_connection, local_port++);
            evhttp_set_timeout(evhttp_connection, 864000); // 10 day timeout
            evhttp_request = evhttp_request_new(reqcb, NULL);
            evhttp_request->chunk_cb = chunkcb;
            sprintf(&path, "");

            if (i % 1000 == 0)
                printf("Req: %s\t->\t%s\n", ori_ip, &path);

            evhttp_add_header(evhttp_request->output_headers, "Host", server_ip);
            evhttp_add_header(evhttp_request->output_headers, "connection", "keep-alive");
            evhttp_add_header(evhttp_request->output_headers, "Content-Length", "0");

            int ret = evhttp_make_request(evhttp_connection, evhttp_request, EVHTTP_REQ_GET, path );
            if (ret == -1)
                printf("evhttp_make_request failed...\n\n");
            else
                printf("evhttp_make_request OK...\n\n");

            evhttp_connection_set_timeout(evhttp_request->evcon, 864000);
            event_loop(EVLOOP_NONBLOCK);

            if ( connected % 1000 == 0 )
                printf("\nChunks: %d\tBytes: %d\tClosed: %d\n", chunks_recvd, bytes_recvd, closed);

            usleep(SLEEP_MS * 10);
        }

        ori_ip = strtok( NULL, delims );
    }

    event_dispatch(); 
    return 0;
}
 
/*
 * 服务器端监听程序
 */
/*
int main2(int argc, char *argv[])
{
    int listen_fd;
    struct sockaddr_in listen_addr;
    struct event ev_accept;
    int reuseaddr_on;

    event_init();
    listen_fd = socket(AF_INET, SOCK_STREAM, 0);
    if (listen_fd < 0) {
        err(1, "listen failed");
    }

    memset(&listen_addr, 0, sizeof(listen_addr));
    listen_addr.sin_family = AF_INET;
    listen_addr.sin_addr.s_addr = INADDR_ANY;
    listen_addr.sin_port = htons(SERVER_PORT);
    if (bind(listen_fd, (struct sockaddr *)&listen_addr, sizeof(listen_addr)) < 0) {
        err(1, "bind failed");
    }

    if (listen(listen_fd, 5) < 0){
        err(1, "listen failed");
    }

    reuseaddr_on = 1;
    setsockopt(listen_fd, SOL_SOCKET, SO_REUSEADDR, &reuseaddr_on, sizeof(reuseaddr_on));

    if (setnonblock(listen_fd) < 0) {
        err(1, "failed to set server to non-blocking");
    }

    event_set(&ev_accept, listen_fd, EV_READ | EV_PERSIST, on_accept, NULL);
    event_add(&ev_accept, NULL);

    event_dispatch();
    return 0;
}
*/
