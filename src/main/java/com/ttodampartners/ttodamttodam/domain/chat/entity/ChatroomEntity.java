//package com.ttodampartners.ttodamttodam.domain.chat.entity;
//
//import com.ttodampartners.ttodamttodam.domain.post.entity.PostEntity;
//import com.ttodampartners.ttodamttodam.domain.user.dto.model.BaseEntity;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import org.hibernate.envers.AuditOverride;
//
//import java.util.List;
//
//@Getter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@AuditOverride(forClass = BaseEntity.class)
//@Entity(name = "chatroom")
//public class ChatroomEntity extends BaseEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id", nullable = false)
//    private Long chatroomId;
//
//    // POST 테이블과 연결
//    @ManyToOne
//    @JoinColumn(name = "post_id", nullable = false)
//    private PostEntity postEntity;
//
//    // CHATROOM_MEMBER 테이블과 연결
//    @OneToMany(mappedBy = "chatroomEntity", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<ChatroomMemberEntity> chatroomMembersEntityList;
//
//    // CHAT_MESSAGE 테이블과 연결
//    @OneToMany(mappedBy = "chatroomEntity", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<ChatMessageEntity> chatMessageEntityList;
//
//    @Column(name = "chat_name")
//    private String chatName;
//
//    @Column(name = "user_count")
//    private int userCount;
//}
