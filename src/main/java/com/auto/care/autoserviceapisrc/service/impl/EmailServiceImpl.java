package com.auto.care.autoserviceapisrc.service.impl;

import com.auto.care.autoserviceapisrc.exception.AutoServiceException;
import com.auto.care.autoserviceapisrc.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class EmailServiceImpl implements EmailService {
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Value( "${email.server.username}" )
    private String emailServerUsername;

    @Value( "${email.server.app.password}" )
    private String emailServerAppPassword;

    @Value( "${email.server.host}" )
    private String emailServerHost;

    @Value( "${email.server.port}" )
    private String emailServerPort;

    @Value( "${email.server.auth}" )
    private String emailServerAuth;

    @Value( "${email.server.starttls.enable}" )
    private String emailServerTtlsEnable;

    @Value( "${email.internal.address}" )
    private String emailInternalAddress;

    @Value( "${email.server.protocol}" )
    private String emailServerProtocol;

    @Value( "${email.server.ssl.factory}" )
    private String emailServerSslFactory;

    @Override
    public void sendEmail(String toAddress, String content, String topic) throws AutoServiceException {
        logger.debug("Sending email to:{} with topic:{}", toAddress, topic);
        Properties prop = new Properties();
        prop.put("mail.smtp.host", emailServerHost);
        prop.put("mail.smtp.port", emailServerPort);
        prop.put("mail.smtp.socketFactory.class", emailServerSslFactory);
        prop.put("mail.smtp.auth", emailServerAuth);
        prop.put("mail.smtp.starttls.enable", emailServerTtlsEnable);
        prop.put("mail.smtp.ssl.protocols", emailServerProtocol);

        Session session = Session.getDefaultInstance(prop,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(emailServerUsername, emailServerAppPassword);
                    }
                });
        try {

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailInternalAddress));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(toAddress)
            );
            message.setSubject("Email confirmation " + topic);
            message.setContent(
                    "<style>    .button {        background-color: #FFB201;     " +
                            "   border: none;        color: white;        padding: 15px 32px;      " +
                            "  text-align: center;        text-decoration: none;        display: inline-block;    " +
                            "    font-size: 16px;        margin: 4px 2px;        cursor: pointer;        opacity: .8; " +
                            "   }    .button:hover {        opacity: 1;    }</style><div style=\"max-width: 800px; " +
                            "margin: auto\">    <div style=\"background-color: #FFF; text-align: center;\">    " +
                            "    <h1 style=\"color: #35B7E8!important; padding-top: 40px;\">Email Confirmation for "
                            + topic + " !</h1>    </div>    <div style=\"text-align: center; max-width: 700px; " +
                            "margin: 40px auto; color: #868686 \"> " + content +
                            "   </div>    <div style=\"text-align: center; max-width: 700px; margin: 40px auto; " +
                            "color: #868686 \">        <p style=\"font-size: 17px;\">          " +
                            "  <br>Yours sincerely,<br>            Auto Care Service Team<br>       " +
                            "    </p>    </div></div>",
                    "text/html");
            Transport.send(message);
            logger.debug("Successfully sent the email");
        }
        catch ( Exception e ) {
            logger.error("Failed to send the email confirmation of:{}", topic, e);
            throw new AutoServiceException("607", String.format("Failed to send the email confirmation of:%s -> %s", topic, e.getMessage()));
        }//HttpStatus.INTERNAL_SERVER_ERROR
    }
}
