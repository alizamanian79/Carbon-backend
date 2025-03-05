
package com.gym.server.model;

import jakarta.persistence.*;
        import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table
@Entity
public class InternalPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account accountId;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course courseId;

    private Long coachId;

    private Long discount;
    private Long amount;
    private String status;
    private String transactionId;

    private LocalDateTime startAt;
    private LocalDateTime endedAt;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;
}
