package com.ttodampartners.ttodamttodam.domain.post.entity;

import com.ttodampartners.ttodamttodam.domain.notification.entity.NotificationEntity;
import com.ttodampartners.ttodamttodam.domain.user.entity.UserEntity;
import com.ttodampartners.ttodamttodam.global.config.StringListConverter;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EnableJpaAuditing
@Builder
@EntityListeners(AuditingEntityListener.class)
@Entity(name = "post")
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long postId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ProductEntity> products = new ArrayList<>();

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer participants;

    @Column(nullable = false)
    private String place;

    @Column(name = "p_location_x", nullable = false)
    private Double pLocationX;

    @Column(name = "p_location_y", nullable = false)
    private Double pLocationY;

    @Column(nullable = false)
    private LocalDateTime deadline;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "purchase_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PurchaseStatus purchaseStatus;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
     private Category category;

    @Lob
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Convert(converter = StringListConverter.class)
    @Builder.Default
    @Column(name = "post_img_url", nullable = false)
    private List<String> imgUrls = new ArrayList<>();

    @Column(name = "create_at", nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "update_at", nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<NotificationEntity> notificationEntity;

    @Getter
    public enum Category {
        DAILY("생활용품"),
        KITCHEN("주방용품"),
        FOOD("식품"),
        PET("반려동물용품"),
        CLOTHING("의류/잡화"),
        HEALTH("헬스/건강식품"),
        OFFICE("오피스/문구"),
        OTHER("기타");

        private final String label;
        Category(String label) {

            this.label = label;
        }
        public static Category fromLabel(String label) {
            for (Category category : Category.values()) {
                if (category.label.equals(label)) {
                    return category;
                }
            }
            throw new IllegalArgumentException("Unknown request status label: " + label);
        }
    }

    @Getter
    public enum Status {
        IN_PROGRESS("진행중"),
        COMPLETED("완료"),
        FAILED("실패");

        private final String label;
        Status(String label) {

            this.label = label;
        }
    }

    @Getter
    public enum PurchaseStatus {
        PREPARING("진행전"),
        PROCEEDING("진행중"),
        SUCCESS("성공"),
        FAILURE("실패");

        private final String label;
        PurchaseStatus(String label) {

            this.label = label;
        }
        public static PurchaseStatus fromLabel(String label) {
            for (PurchaseStatus purchaseStatus : PurchaseStatus.values()) {
                if (purchaseStatus.label.equals(label)) {
                    return purchaseStatus;
                }
            }
            throw new IllegalArgumentException("Unknown request status label: " + label);
        }
    }

}
