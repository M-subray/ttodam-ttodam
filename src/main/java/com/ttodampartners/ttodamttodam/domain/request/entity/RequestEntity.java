package com.ttodampartners.ttodamttodam.domain.request.entity;

import com.ttodampartners.ttodamttodam.domain.post.entity.PostEntity;
import com.ttodampartners.ttodamttodam.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;



@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EnableJpaAuditing
@Builder
@EntityListeners(AuditingEntityListener.class)
@Entity(name = "request")
public class RequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long requestId;

    @ManyToOne
    @JoinColumn(name = "request_user", nullable = false)
    private UserEntity requestUser;

    @JoinColumn(name = "request_member_manner_evaluated")
    private boolean requestMemberMannerEvaluated;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity post;

    @Column(name = "request_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private RequestEntity.RequestStatus requestStatus;

    @Column(name = "create_at", nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "update_at", nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Getter
    public enum RequestStatus {
        WAIT("대기"),
        ACCEPT("수락"),
        REFUSE("거절");

        private final String label;
        RequestStatus(String label) {

            this.label = label;
        }
        public static RequestStatus fromLabel(String label) {
            for (RequestStatus status : RequestStatus.values()) {
                if (status.label.equals(label)) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Unknown request status label: " + label);
        }
    }
}
