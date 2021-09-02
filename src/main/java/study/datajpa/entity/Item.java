package study.datajpa.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Item implements Persistable<String> {

  @Id private String id;

  @CreatedDate
  @Column(updatable = false)
  private LocalDateTime createDate;

  @PrePersist
  public void generateId() {
    id = CustomId.generate();
  }

  public String getId() {
    return id;
  }

  @Override
  public boolean isNew() {
    return createDate == null;
  }

  public void setId(String id) {
    this.id = id;
  }

  public LocalDateTime getCreateDate() {
    return createDate;
  }
}
