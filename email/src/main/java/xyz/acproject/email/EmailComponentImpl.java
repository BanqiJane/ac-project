package xyz.acproject.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Component
public class EmailComponentImpl implements EmailComponent {


        private final Logger logger = LoggerFactory.getLogger(this.getClass());
//        static final String DELIM_STR = "{}";

        private JavaMailSender mailSender;

        @Value("${spring.mail.username}")
        private String from;

//        MailMessage mailMessage;

        public void sendSimpleMail(String subject, String content,String... to) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);

            try {
                mailSender.send(message);
                logger.info("简单邮件已经发送。");
            } catch (Exception e) {
                logger.error("发送简单邮件时发生异常！", e);
            }

        }

        public void sendHtmlMail(String subject, String content,String... to) {
            MimeMessage message = mailSender.createMimeMessage();

            try {
                // true表示需要创建一个multipart message
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setFrom(from);
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setText(content, true);

                mailSender.send(message);
                logger.info("html邮件发送成功");
            } catch (Exception e) {
                logger.error("发送html邮件时发生异常！", e);
            }
        }

        public void sendInlineResourceMail(String subject, String content, String rscPath, String rscId,String... to) {
            MimeMessage message = mailSender.createMimeMessage();

            try {
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setFrom(from);
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setText(content, true);

                FileSystemResource res = new FileSystemResource(new File(rscPath));
                helper.addInline(rscId, res);

                mailSender.send(message);
                logger.info("嵌入静态资源的邮件已经发送。");
            } catch (MessagingException e) {
                logger.error("发送嵌入静态资源的邮件时发生异常！", e);
            }
        }

        public void sendAttachmentsMail(String to, String subject, String content, String filePath) {
            MimeMessage message = mailSender.createMimeMessage();

            try {
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setFrom(from);
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setText(content, true);

                FileSystemResource file = new FileSystemResource(new File(filePath));
                String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
                helper.addAttachment(fileName, file);

                mailSender.send(message);
                logger.info("带附件的邮件已经发送。");
            } catch (MessagingException e) {
                logger.error("发送带附件的邮件时发生异常！", e);
            }
        }

//        public MailMessage getMailMessage() {
//            return mailMessage;
//        }
//
//        public void setMailMessage(MailMessage mailMessage) {
//            this.mailMessage = mailMessage;
//        }

//        public void sendHtml(String content, MailType... mailTypes) throws IOException {
//            MimeMessage message = mailSender.createMimeMessage();
//            try {
//                MimeMessageHelper helper = new MimeMessageHelper(message, true);
//                helper.setFrom(mailMessage.getFrom());
//                helper.setTo(mailMessage.getTo());
//                helper.setSubject(mailMessage.getSubject());
//                String msg = getContent(content, mailTypes);
//                System.out.println(msg);
//                helper.setText(msg, true);
//                for (MailType item : mailTypes) {
//                    switch (item.getType()) {
//                        case MailType.TYPE_FILE:
//                            InlineFile inlineFile = (InlineFile) item;
//                            helper.addInline(inlineFile.getCid(), new File(inlineFile.getFilePath()));
//                            break;
//                        case MailType.TYPE_ATTACH:
//                            AttachFile attachFile = (AttachFile) item;
//                            helper.addAttachment(attachFile.getFileName(), new File(attachFile.getFilePath()));
//                            break;
//                    }
//                }
//
////			mailSender.send(message);
//                logger.info("带附件的邮件已经发送。");
//            } catch (MessagingException e) {
//                logger.error("发送带附件的邮件时发生异常！", e);
//            }
//        }

//        public String getContent(String content, MailType... mailTypes)
//                throws MessagingException, IOException {
//            String bodyPrefix = "<html><body>";
//            String bodySuffix = "</body></html>";
//            StringBuffer sb = new StringBuffer();
//            sb.append(bodyPrefix);
//            for (MailType item : mailTypes) {
//                if(content.length() < 1)break;
//
//                int index = content.indexOf(DELIM_STR);
//                if(index == -1)break;
//                sb.append(content.substring(0, index));
//                switch (item.getType()) {
//                    case MailType.TYPE_FILE:
//                        InlineFile inlineFile = (InlineFile) item;
//                        sb.append("<img src=\'cid:" + inlineFile.getCid() + "\' />");
//                        break;
//                    case MailType.TYPE_TEXT:
//                        TextString textString = (TextString) item;
//                        sb.append(textString.getText());
//                        break;
//                    case MailType.TYPE_JSON:
//                        JsonTable json = (JsonTable) item;
//                        sb.append(genReportData(json));
//                        break;
//                }
//                content = content.substring(index + 2);
//            }
//            sb.append(content);
//            sb.append(bodySuffix);
//            return sb.toString();
//        }
//
//        private String read(String filePath) throws IOException {
//            String encoding = "UTF-8";
//            File tmpFile = new File(filePath);
//            Long filelength = tmpFile.length();
//            byte[] filecontent = new byte[filelength.intValue()];
//            FileInputStream in = new FileInputStream(tmpFile);
//            in.read(filecontent);
//            in.close();
//            String fileJson = new String(filecontent, encoding);
//            return fileJson;
//        }
//
//        private String genReportData(JsonTable jsonTable) throws IOException {
//            JSONArray ja = (JSONArray) JSON.parse(read(jsonTable.getData()),Feature.OrderedField);
//            StringBuilder sb = new StringBuilder();
//            try {
//                sb.append("<br />\n");
//                sb.append("<table border=\"1\" style=\"border-collapse:collapse;font-size:14px\">\n");
//                sb.append("<caption align = \"left\">");
//                sb.append(jsonTable.getTitle());
//                sb.append("</caption>\n");
//                JSONObject jsonFirst = (JSONObject) ja.get(0);
//
//                sb.append("<tr>\n");
//                for(String key : jsonFirst.keySet()){
//                    sb.append("<td>");
//                    sb.append(jsonFirst.get(key));
//                    sb.append("</td>\n");
//                }
//
//                sb.append("</tr>\n");
//                ja.remove(0);
//                for (Object column : ja) {
//                    sb.append("<tr>\n");
//                    JSONObject json = (JSONObject) column;
//                    for(String key : jsonFirst.keySet()){
//                        sb.append("<td>");
//                        sb.append(json.get(key));
//                        sb.append("</td>\n");
//                    }
//
//                    sb.append("</tr>\n");
//                }
//
//                sb.append("</table>\n");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return sb.toString();
//        }


    @Autowired
    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
}
