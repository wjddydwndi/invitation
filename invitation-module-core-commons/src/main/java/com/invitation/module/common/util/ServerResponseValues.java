package com.invitation.module.common.util;

public enum ServerResponseValues {

    /** 1xx 조건부 응답 **/
    SVR_RES_CODE_100("100", "CONTINUE", "continue"), // 지금까지 상태가 괜찮으며 클라이언트가 계속해서 요청을 하거나 이미 요청을 완료한 경우에는 무시해도 되는 것을 알려준다.
    SVR_RES_CODE_101("101", "SWITCHING PROTOCOL", "Switching protocol"),// 요청자가 서버에 프로토콜 전환을 요청했으며 서버는 이를 승인하는 중이다.
    SVR_RES_CODE_102("102", "PROCESSING", "processing"), // (Processing : 처리) 서버가 요청을 수신하였으며 이를 처리하고 있지만 아직 제대로된 응답을 알려줄 수 없음을 알려준다.

    /** 2xx 성공 응답 **/
    SVR_RES_CODE_200("200", "SUCCESS", "OK"), // 요청이 성공적으로 되었다. 서버가 요청한 페이지를 제공했다는 의미로 쓰인다
    SVR_RES_CODE_201("201", "CREATED", "The request was successful and the server created a new resource."),// 성공적으로 요청되었으며 서버가 새 리소스를 작성했다. POST 요청 또는 일부 PUT 요청 이후 따라온다.
    SVR_RES_CODE_202("202", "ACCEPTED", "The server has received the request but has not yet processed it."), // 서버가 요청을 접수했지만 아직 처리하지 않았다. 다른 프로세스에서 처리 또는 서버가 요청을 다루고 있거나 배치 프로세스를 하고 있는 경우를 위해 만들어졌다.
    SVR_RES_CODE_203("203", "NON-AUTHORITATIVE INFOMATION", "The server has successfully processed the request but does not provide content."), // 신뢰할 수 없는 정보, 서버가 요청을 성공적으로 처리했지만 콘텐츠를 제공하지 않는다.
    SVR_RES_CODE_204("204", "REQEUST CONTENT", "Request Content"), // 콘텐츠 없음, 서버가 요청을 성공적으로 처리했지만 콘텐츠를 제공하지 않음.
    SVR_RES_CODE_205("205", "RESET CONTENT", "Reset Content "), // 요청을 완수한 이후에 사용자 에이전트에게 이 요청을 보낸 문서 뷰를 리셋하라고 알려준다.
    SVR_RES_CODE_206("206", "PARTIAL CONTENT", "The server successfully processed only a portion of the GET request."), // 서버가 GET 요청의 일부만 성공적으로 처리했다.
    SVR_RES_CODE_207("207", "MULTI-STATUS", "Multi-Status"), // 멀티-상태 응답은 여러 리소스가 여러 상태 코드인 상황이 적절한 경우에 해당되는 정보를 전달한다.
    SVR_RES_CODE_208("208", "MULTI-STATUS", "Multi-Status"), // DAV에서 사용된다. propstatproperty와 status의 합성어) 응답 속성으로 동일 컬렉션으로 바인드된 복수의 내부 멤버를 반복적으로 열거하는 것을 피하기 위해 사용된다.

    SVR_RES_CODE_226("226", "PARTIAL CONTEN", "IM Used"), // 서버가 GET 요청에 대한 리소스 의무를 다 했고 그 응답이 하나 또는 그 이상의 인스턴스 조작이 현재 인스턴스에 적용이 되었음을 알려준다.

    /** 3xx 리다이렉션 완료 **/
    SVR_RES_CODE_300("300", "MULTI CHOICE", "Multi Choice"), // 서버가 요청에 대해서 하나 이상의 응답을 할 수 있다. 사용자 에이전트 또는 사용자는 그 중에 하나를 반드시 선택해야 한다.
    SVR_RES_CODE_301("301", "MOVED PERMANTLY", "Resource change for request URI (moved Permantly)"),// 요청한 리소스의 URI가 변경되었음을 의미한다. GET 또는 HEAD 요청에 대한 응답으로 이 응답 표시하면 요청자는 자동으로 새 위치 전달된다.
    SVR_RES_CODE_302("302", "FOUND(TEMPORARY MOVE)", "Resource change for request URI (Temporary move)"), // 요청한 리소스의 URI가 일시적으로 변경되었음을 의미한다. 새롭게 변경된 URI는 나중에 만들어질 수 있다. 클라이언트는 향후의 요청시 원래 위치 계속 사용해야 한다.
    SVR_RES_CODE_303("303", "SEE OTHER", "see other"), // 클라이언트가 요청한 리소스를 다른 URI에서 GET 요청을 통해 얻어야 할 때 서버가 클라이언트로 직접 보내는 응답이다.
    SVR_RES_CODE_304("304", "NOT MODIFIED", "Not Modified"), // 마지막 요청 이후 요청한 페이지는 수정되지 않았다. 서버가 이 응답을 표시하면 페이지의 콘텐츠 표시하지 않는다.
    SVR_RES_CODE_305("305", "USE PROXY", "Use Proxy"), //  요청자는 프록시를 사용하여 요청한 페이지만 액세스할 수 있다.
    SVR_RES_CODE_306("307", "TEMPORARY REDIRECT", "Temporary Redirect"), // 현재 서버가 다른 위치의 페이지로 요청에 응답하고 있지만 요청자는 향후 요청 시 원래 위치를 계속 사용해야 한다
    SVR_RES_CODE_308("308", "PERMANENT REDIRECT", "Permanent Redirect"), // 리소스가 이제 HTTP 응답 헤더의 Location:에 명시된 영구히 다른 URI에 위치하고 있음을 의미.

    /** 4xx 요청오류 **/
    SVR_ERR_CODE_400("400", "BAD REQUEST", "Request server with incorrect grammar"), // 잘못된 문법으로 서버 요청
    SVR_ERR_CODE_401("401", "UNAUTHORIZED", "Certification or approval required"),// 미승인 / 비인증 > 클라이언트는 요청한 응답을 받기 위해서는 반드시 스스로를 인증해야 함.
    SVR_ERR_CODE_403("403", "FORBIDDEN", "Certification or approval required"), // 미승인/비인증, 401과 다른점 : 클라이언트가 누군지 알고 있음.
    SVR_ERR_CODE_404("404", "NOT FOUND", "The requested resource could not be found."), // 요청받은 리소스를 찾을 수 없음.
    SVR_ERR_CODE_405("405", "METHOD NOT ALLOWED", "The method is not available."), // 요청한 메서드가 제거되어 사용할 수 없음.
    SVR_ERR_CODE_406("406", "NOT ACCEPTABLE", "NOT ACCEPTABLE"), // 사용자 에이전트의 규칙에 따라 어떠한 컨텐츠도 찾지 않았을 경우 웹서버가 보냄.
    SVR_ERR_CODE_407("407", "PROXY AUTHENTICATION REQUIRED", "Authentication required"), // 프록시에 의해 완료된 인증이 필요함.
    SVR_ERR_CODE_408("408", "REQEUST TIMEOUT", "request timeout"), // 서버가 사용되지 않는 연결을 끊음.
    SVR_ERR_CODE_409("409", "CONFLICT", "conflict"), // 요청이 현재 서버의 상태와 충돌
    SVR_ERR_CODE_410("410", "GONE", "Deleted content or url"), // 요청한 콘텐츠가 서버에서 영구적으로 삭제되었을 경우, 클라이언트가 캐시와 리소스를 지워야함. 일시적인 홍보용 서비스 사용하는 경우
    SVR_ERR_CODE_411("411", "LENGTH REQUIRED", "Content-Length Header Required"), // 서버에서 필요로 하는 Content-Length 헤더 필드가 정의되지 않은 요청이 들어옴.
    SVR_ERR_CODE_412("412", "PRECONDITION FAILED", "Invalid header"), // 서버가 요청자가 요청시 부과한 사전조건을 만족하지 않음.
    SVR_ERR_CODE_413("413", "PAYLOAD_TOO_LARGE", "Request payload too large"), // 요청이 너무 커서 서버가 처리할 수 없음.
    SVR_ERR_CODE_414("414", "URI TOO LONG", "uri too long"), // 요청 URI(일반적으로 URL)가 너무 길어 서버가 처리할 수 없음.
    SVR_ERR_CODE_415("415", "UNSUPPORTED MEDIA TYPE", "The requested media format is not supported by the server"), // 요청한 미디어 포맷은 서버에서 지원하지 않습니다. 서버는 해당 요청을 거절할 것입니다.
    SVR_ERR_CODE_416("416", "REQUESTED RANGE NOT SATISFIABLE", "requested range not satisfiable"), // 요ㅕ청이 페이지에서 처리할 수 없는 범위에 해당되는 경우
    SVR_ERR_CODE_417("417", "EXPECTATION FAILED", "Expectation Failed"), // 서버는 Expect 요청 헤더 입력란의 요구사항을 만족할 수 없다.
    SVR_ERR_CODE_421("421", "MISDIRECTED REQEUST", "Misdirected Request"), // 요청이 응답을 생성할 수 없는 서버로 지정. URL에 포함된 스키마와 권한의 조합에 대한 응답을 생성하도록 구성되지 않은 서버에서 전송
    SVR_ERR_CODE_422("422", "UNPROCESSABLE ENTITY", "Unprocessable Entity"), // 요청은 잘 형성되었지만 의미적 오류로 인해 추적할 수 없다.
    SVR_ERR_CODE_423("423", "LOCKED", "Resource is locked."), // 접근하고자 하는 리소스가 잠김
    SVR_ERR_CODE_426("426", "UPGRADE REQUIRED", "Upgrade Required"), // 클라이언트는 업그레이드 헤더 필드에 주어진 프로토콜로 요청해야 함.
    SVR_ERR_CODE_428("428", "PRECONDITION REQUIRED", "PreConditaion Required"), // 요청을 조건부로 요구
    SVR_ERR_CODE_429("429", "TOO MANY REQUESTS", "Too Many Requests"), // 사용자가 일정 시간 동안 너무 많은 요청을 보냄
    SVR_ERR_CODE_431("431", "REQUEST HEADER FIELDS TOO LARGE", "Request header is too large. Please shorten the header and request it again"), // 헤더 필드가 너무 크기 때문에 서버가 요청을 처리하지 않음.

    /** 5xx 서버오류 **/
    SVR_ERR_CODE_500("500", "INTERNAL SERVER ERROR", "The server encountered an error and could not perform the request."), // 서버에 오류가 발생하여 요청을 수행할 수 없다.
    SVR_ERR_CODE_501("501", "NOT IMPLEMENTED", "There is no functionality to perform a request"), // 서버에 요청을 수행할 수 있는 기능이 없다. 예를 들어 요청 메서드를 인식하지 못했을 경우
    SVR_ERR_CODE_502("502", "BAD GATEWAY", "Bad Gateway"), // 서버가 게이트웨이나 프록시 역할을 하고 있거나 또는 업스트림 서버에서 잘못된 응답을 받았다.
    SVR_ERR_CODE_504("504", "GATEWAY TIMEOUT", "Gateway Timeout"), // 서버가 게이트웨이나 프록시 역할을 하고 있거나 또는 업스트림 서버에서 제때 요청을 받지 못했을 때 발생한다.
    SVR_ERR_CODE_505("505", "HTTP VERSION NOT SUPPORTED", "http version not supported"), // 서버가 요청에 사용된 HTTP 프로토콜 버전을 지원하지 않는다.
    SVR_ERR_CODE_506("506", "VARIANT ALSO NEGOTITATES", "Server internal configuration error"), // 서버에 내부 구성 오류가 있다. 요청에 대한 투명한 내용 협상으로 인해 순환 참조가 발생한다.
    SVR_ERR_CODE_507("507", "INSUFFICIENT STORAGE", "Insufficient Storage"), // 용량 부족
    SVR_ERR_CODE_508("508", "LOOP DETECTED", "Loop Detected"), // 서버가 요청을 처리하는 동안 무한 루프 감지.
    SVR_ERR_CODE_509("509", "LIMITED EXTENSION", "Apache bw/limited extension"), // 대역폭 제한 초과
    SVR_ERR_CODE_510("510", "NOT EXTENDED", "An extension is required on the server for processing requests."), // 서버가 요청을 처리하기 위해서는 더 확장해야함.
    SVR_ERR_CODE_511("511", "NETWORK AUTHENTICATION REQUIRED", "Network Authentication Required"), // 네트워크 인증 필요
    SVR_ERR_CODE_598("598", "NETWORK READ TIMEOUT", "Network Read Timeout"), // 네트워크 읽기 시간 초과 오류 : 알 수 없음
    SVR_ERR_CODE_599("599", "NETWORK CONNECTION TIMEOUT", "Network connection timeout"); // 네트워크 연결 시간 초과 오류 : 알 수 없음.

    private String code;
    private String value;
    private String message;

    ServerResponseValues(String code, String value, String message) {
        this.code = code;
        this.value = value;
        this.message = message;
    }

    public String CODE()        {  return code;    }
    public String VALUE()       {  return value;   }
    public String MESSAGE()     {  return message;   }

}