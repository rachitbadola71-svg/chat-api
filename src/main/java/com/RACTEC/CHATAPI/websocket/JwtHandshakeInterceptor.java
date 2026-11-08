package com.RACTEC.CHATAPI.websocket;

import com.RACTEC.CHATAPI.Service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.Map;

@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtService jwtService;
    public JwtHandshakeInterceptor(JwtService jwtService) {this.jwtService = jwtService;}

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        System.out.println("GUARD REACHED: " + request.getURI());
        MultiValueMap<String,String> params =
                UriComponentsBuilder.fromUri(request.getURI()).build().getQueryParams();

        String token = params.getFirst("token");
        String roomIdText = params.getFirst("roomId");

        if (token == null || roomIdText == null) {response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return false;}

        try{
            Long userId = jwtService.getUserId(token);
            Long roomId = Long.parseLong(roomIdText);

            attributes.put("userId", userId);
            attributes.put("roomId", roomId);
            return true ;
        }catch(Exception e){
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }
    }
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {

    }
}
