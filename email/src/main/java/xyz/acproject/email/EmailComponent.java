package xyz.acproject.email;

public interface EmailComponent {

    void sendSimpleMail(String subject, String content,String... to);

    void sendHtmlMail(String subject, String content,String... to);

    void sendInlineResourceMail(String subject, String content, String rscPath, String rscId,String... to);

//    public void setMailMessage(MailMessage mailMessage);
//
//    public void sendHtml(String content, MailType... mailTypes) throws IOException;
}
