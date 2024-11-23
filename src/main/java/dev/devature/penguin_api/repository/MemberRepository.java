package dev.devature.penguin_api.repository;

import dev.devature.penguin_api.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByAppUser_EmailAndOrganizationId(@Param("email") String email, @Param("orgId") Long orgId);
}
