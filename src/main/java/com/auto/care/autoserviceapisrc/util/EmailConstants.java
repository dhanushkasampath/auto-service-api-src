package com.auto.care.autoserviceapisrc.util;

public class EmailConstants {

    public static final String EMAIL_SMTP_HOST = "mail.smtp.host";
    public static final String EMAIL_SMTP_PORT = "mail.smtp.port";
    public static final String EMAIL_SMTP_AUTH = "mail.smtp.auth";
    public static final String EMAIL_SMTP_TTL_ENABLE = "mail.smtp.starttls.enable";

    public static final String INVITATION_EMAIL_CONTENT = "<p style=\"font-size: 17px;\">          Please click the following link to confirm your email address        </p>" +
            " </div>    <div style=\"text-align: center\"> <a href=\" %s \"class=\"button\">Verify Email Address</a> ";
    public static final String INVITATION_EMAIL_CONTACT_ADMIN_CONTENT = "You have not completed the first login yet to reset password. Please contact an authorized person to resend the first login confirmation email.";

}
