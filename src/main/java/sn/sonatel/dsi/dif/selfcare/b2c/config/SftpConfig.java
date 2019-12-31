package sn.sonatel.dsi.dif.selfcare.b2c.config;

import com.jcraft.jsch.ChannelSftp.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.file.FileNameGenerator;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.sftp.outbound.SftpMessageHandler;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;

import java.io.File;

@Configuration
public class SftpConfig {

    @Value("${application.serveur-ftp.ip-serveur}")
    private String sftpHost;

    @Value("${application.serveur-ftp.port}")
    private int sftpPort;

    @Value("${application.serveur-ftp.user}")
    private String sftpUser;

    @Value("${application.serveur-ftp.password}")
    private String sftpPasword;

    @Value("${application.serveur-ftp.directory}")
    private String sftpRemoteDirectory;

    @Bean
    public SessionFactory<LsEntry> sftpSessionFactory() {
        DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory(true);

        factory.setHost(sftpHost);
        factory.setPort(sftpPort);
        factory.setUser(sftpUser);
        factory.setPassword(sftpPasword);
        factory.setAllowUnknownKeys(true);

        return new CachingSessionFactory<LsEntry>(factory);
    }
    @Bean
    @ServiceActivator(inputChannel = "toSftpChannel")
    public MessageHandler handler() {
        SftpMessageHandler handler = new SftpMessageHandler(sftpSessionFactory());
        handler.setRemoteDirectoryExpression(new LiteralExpression(sftpRemoteDirectory));
        handler.setFileNameGenerator(new FileNameGenerator() {

            @Override
            public String generateFileName(Message<?> message) {
                if (message.getPayload() instanceof File) {
                    return ((File) message.getPayload()).getName();
                } else {
                    throw new IllegalArgumentException("File expected as payload.");
                }
            }

        });
        return handler;
    }


    @MessagingGateway
    public interface UploadGateways {

        @Gateway(requestChannel = "toSftpChannel")
        boolean upload(File file);
    }
}
