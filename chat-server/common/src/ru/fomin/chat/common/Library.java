package ru.fomin.chat.common;

public class Library {
    /*
     * /auth_request±login±password
     * /auth_accept±nickname
     * /auth_denied
     * /broadcast±time±src±msg
     * /msg_format_error
     * /user_list±user1±user2±user3±....
     * /client_bcast±msg
     * /client_private±recipient±msg
     * /client_snooze
     * /
     * */

    public static final String DELIMITER = "±";
    public static final String AUTH_REQUEST = "/auth_request";
    public static final String AUTH_ACCEPT = "/auth_accept";
    public static final String AUTH_DENIED = "/auth_denied";
    public static final String MSG_FORMAT_ERROR = "/msg_format_error"; // если мы вдруг не поняли, что за сообщение и не смогли разобрать
    public static final String TYPE_BROADCAST = "/bcast"; // то есть сообщение, которое будет посылаться всем
    public static final String TYPE_PRIVATE = "/private";
    public static final String USER_LIST = "/user_list";
    public static final String TYPE_BCAST_CLIENT = "/client_bcast";
    public static final String TYPE_ERROR_SENDING_YOURSELF="/error_by_sending_yourself";
    public static final String REGISTRATION="/registration";
    public static final String REGISTRATION_SUCCESSFULLY = "/registration_successfully";
    public static final String REGISTRATION_NOT_SUCCESSFULLY = "/registration_not_successfully";
    public static final String NICKNAME_WAS_CHANGED = "/nickname_was_changed";
    public static final String CHANGING_NICKNAME = "/changing_nickname";
    public static final String CHANGING_PASSWORD= "/changing_password";
    public static final String CHANGING_PASSWORD_ERROR= "/changing_password_error";
    public static final String SERVER="Server";


    public static String getAuthRequest(String login, String password) {
        return AUTH_REQUEST + DELIMITER + login + DELIMITER + password;
    }

    public static String getAuthAccept(String nickname) {
        return AUTH_ACCEPT + DELIMITER + nickname;
    }

    public static String getAuthDenied() {
        return AUTH_DENIED;
    }

    public static String getMsgFormatError(String message) {
        return MSG_FORMAT_ERROR + DELIMITER + message;
    }

    public static String getTypeBroadcast(String src, String message) {
        return TYPE_BROADCAST + DELIMITER + System.currentTimeMillis() +
                DELIMITER + src + DELIMITER + message;
    }

    public static String getTypePrivate(String src, String message) {
        return TYPE_PRIVATE + DELIMITER + System.currentTimeMillis() +
                DELIMITER + src + DELIMITER + message;
    }

    public static String getTypeClientPrivate(String destination, String message) {
        return TYPE_PRIVATE + DELIMITER + destination + DELIMITER + message;
    }

    public static String getUserList(String users) {
        return USER_LIST + DELIMITER + users;
    }

    public static String getTypeClientBcast(String msg) {
        return TYPE_BCAST_CLIENT + DELIMITER + msg;
    }
    public static String getErrorBySendingYourself(){
        return TYPE_ERROR_SENDING_YOURSELF;
    }

    public static String getRegistrationMessage(String login, String password, String nickName) {
        return REGISTRATION+DELIMITER+login+DELIMITER+password+DELIMITER+nickName;
    }

    public static String getChangingNicknameMessage(String newNickname) {
        return CHANGING_NICKNAME+DELIMITER+newNickname;
    }
    public static String  getChangingPasswordMessage(String password,String newPassword){
        return CHANGING_PASSWORD+DELIMITER+password+DELIMITER+newPassword;
    }
}
