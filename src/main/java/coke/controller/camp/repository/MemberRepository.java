package coke.controller.camp.repository;

import coke.controller.camp.entity.Board;
import coke.controller.camp.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {

    @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Member m WHERE m.fromSocial = :social AND m.email = :email")
    Optional<Member> findByEmail(@Param("email") String email, @Param("social") boolean social);

    @EntityGraph(attributePaths = "roleSet")
    @Query("SELECT m FROM Member m WHERE m.email = :email AND m.fromSocial = false ")
    Optional<Member> getWithRole(String email);

    @EntityGraph(attributePaths = "roleSet")
    Optional<Member> findByEmail(String email);

    @Modifying
    @Query("UPDATE Member m SET m.memberName = :name, m.password = :pw WHERE m.email = :email")
    void updateMemberNameAndPasswordByEmail(@Param("name") String name, @Param("pw") String pw, @Param("email") String email);

}
