package com.ttodampartners.ttodamttodam.domain.product.entity;

import com.ttodampartners.ttodamttodam.domain.post.entity.PostEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EnableJpaAuditing
@Builder
@EntityListeners(AuditingEntityListener.class)
@Entity(name = "product")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long productId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity postId;


    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(nullable = false)
    private Long count;

    @Column(name = "purchase_link", nullable = false)
    private String purchaseLink;

    @Column(nullable = false)
    private Long price;

    @Column(name = "product_img_url", nullable = false)
    private String productImgUrl;
}
