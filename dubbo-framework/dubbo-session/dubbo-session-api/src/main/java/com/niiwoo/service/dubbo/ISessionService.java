/**   
* @Title: ISessionService.java
* @Package com.niiwoo.dubbo.session.api
* @Description: TODO(用一句话描述该文件做什么)
* @author seven   
* @date 2016-7-10 下午1:51:05
* @version V1.0   
*/


package com.niiwoo.service.dubbo;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.niiwoo.dto.SessionIdDTO;
import com.niiwoo.dto.UnifiedSession;

/**
 * @ClassName: ISessionService
 * @Description: session接口
 * @author seven
 * @date 2016-7-10 下午1:51:05
 * 
 */
@Path("session")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public interface ISessionService {
    /**
     * 注册一个会话
     *
     * @param session
     */
    @POST
    @Path("add")
    void addSession(UnifiedSession session);

    /**
     * 根据sessionId获取会话
     *
     * @param sessionId
     * @return
     */
    @GET
    @Path("get/{sessionId : \\w+}")
    UnifiedSession getSession(@PathParam("sessionId") String sessionId);

    /**
     * 根据用户和设备获取sessionId
     *
     * @param sessionId
     * @return
     */
    @GET
    @Path("get/{devType : \\w+}/{userId : \\w+}")
    SessionIdDTO getSession(@PathParam("devType") String devType, @PathParam("userId") String userId);
    
    /**
     * 移除这个会话
     *
     * @param sessionId
     * @return
     */
    @GET
    @Path("remove/{sessionId : \\w+}")
    void removeSession(@PathParam("sessionId") String sessionId);
}
