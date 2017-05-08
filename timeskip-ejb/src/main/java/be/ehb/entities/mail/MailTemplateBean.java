package be.ehb.entities.mail;

import be.ehb.mail.MailTopic;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@Entity
@Table(name = "mail_templates", schema = "timeskip")
public class MailTemplateBean implements Serializable {

    @Id
    @Column(name = "topic", nullable = false)
    @Enumerated(EnumType.STRING)
    private MailTopic id;
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "subject", nullable = false)
    private String subject;
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "content", nullable = false)
    private String content;

    public MailTopic getId() {
        return id;
    }

    public void setId(MailTopic id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MailTemplateBean)) return false;

        MailTemplateBean that = (MailTemplateBean) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "MailTemplate{" +
                "id='" + id + '\'' +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}