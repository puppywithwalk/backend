package com.puppypaws.project.repository;

import com.puppypaws.project.entity.Dogstagram;
import com.puppypaws.project.model.IDogstagram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DogstagramRepository extends JpaRepository<Dogstagram, Long> {

    List<Dogstagram> findByMember_Id(Long user_id);
    Optional<Dogstagram> findById(Long id);

    @Query(value =
            "SELECT dogstagram.id AS id," +
            "dogstagram.description AS description," +
            "attachment.url AS url," +
            "attachment.url2 AS url2," +
            "attachment.url3 AS url3," +
            "member.nickname AS nickname," +
            "member.id AS user_id," +
            "member.dog_type AS dog_type," +
            "member.profile_url AS profile_url," +
            "(SELECT nickname     FROM member     " +
                    "WHERE id = (SELECT user_id " +
            "                   FROM dogstagram_like " +
            "                    WHERE dogstagram_id = dogstagram.id " +
            "                   ORDER BY created_at DESC  LIMIT 1)" +
            ") AS last_liked_nickname," +
            "dogstagram.created_at AS created_at," +
            "(SELECT COUNT(m.id) AS total_like FROM dogstagram_like m WHERE m.dogstagram_id = dogstagram.id) AS total_like," +
            "CASE  WHEN EXISTS (SELECT 1 " +
            "                   FROM dogstagram_like dl " +
            "                          WHERE dl.dogstagram_id = dogstagram.id AND dl.user_id = :id           " +
            "                    ) THEN TRUE              " +
            "               ELSE FALSE" +
            "           END AS is_liked," +
            "dogstagram.created_at AS created_at " +
            "FROM dogstagram dogstagram " +
            "INNER JOIN attachment attachment ON dogstagram.attachment_id = attachment.id " +
            "INNER JOIN member member ON member.id = dogstagram.member_id " +
            "ORDER BY dogstagram.created_at DESC " +
            "LIMIT :take " +
            "OFFSET :skip", nativeQuery = true)
    public List<IDogstagram> getDogstagramList(@Param(value = "id") Long id, @Param(value = "take") int take, @Param(value = "skip") int skip);
    @Query(value =
            "SELECT " +
                    "    d.id AS id," +
                    "    d.description AS description," +
                    "    a.url AS url," +
                    "    a.url2 AS url2," +
                    "    a.url3 AS url3," +
                    "    m.nickname AS nickname," +
                    "    m.id AS user_id," +
                    "    m.dog_type AS dog_type," +
                    "    m.profile_url AS profile_url," +
                    "    COALESCE(last_like.nickname, '') AS last_liked_nickname," +
                    "    d.created_at AS created_at," +
                    "    COUNT(dl.id) AS total_like," +
                    "    EXISTS (" +
                    "        SELECT 1" +
                    "        FROM dogstagram_like dl2" +
                    "        WHERE dl2.dogstagram_id = d.id AND dl2.user_id = :id" +
                    "    ) AS is_liked\n" +
                    "FROM " +
                    "    dogstagram d " +
                    "INNER JOIN " +
                    "    attachment a ON d.attachment_id = a.id " +
                    "INNER JOIN " +
                    "    member m ON d.member_id = m.id " +
                    "LEFT JOIN ( " +
                    "    SELECT " +
                    "        dl.dogstagram_id," +
                    "        m.nickname," +
                    "        dl.created_at" +
                    "    FROM ( " +
                    "        SELECT " +
                    "            dogstagram_id, " +
                    "            MAX(created_at) AS max_created_at " +
                    "        FROM " +
                    "            dogstagram_like " +
                    "        GROUP BY " +
                    "            dogstagram_id" +
                    "    ) latest " +
                    "    INNER JOIN " +
                    "        dogstagram_like dl ON latest.dogstagram_id = dl.dogstagram_id AND latest.max_created_at = dl.created_at " +
                    "    INNER JOIN " +
                    "        member m ON dl.user_id = m.id " +
                    ") last_like ON d.id = last_like.dogstagram_id " +
                    "LEFT JOIN " +
                    "    dogstagram_like dl ON d.id = dl.dogstagram_id " +
                    "GROUP BY " +
                    "    d.id, d.description, a.url, a.url2, a.url3, m.nickname, m.id, m.dog_type, m.profile_url, last_like.nickname, d.created_at " +
                    "ORDER BY " +
                    "    total_like DESC, d.created_at DESC " +
                    "LIMIT 4 ", nativeQuery = true)
    public List<IDogstagram> getStarDogstagramList(@Param(value = "id") Long id);

    @Query(value =
    "SELECT" +
            "    dogstagram.id AS id," +
            "    dogstagram.description AS description," +
            "    attachment.url AS url," +
            "    attachment.url2 AS url2," +
            "    attachment.url3 AS url3," +
            "    member.nickname AS nickname," +
            "    member.id AS user_id," +
            "    member.profile_url AS profile_url," +
            "    member.dog_type AS dog_type," +
            "    (SELECT nickname" +
            "     FROM member" +
            "     WHERE id = (SELECT user_id" +
            "                 FROM dogstagram_like" +
            "                 WHERE dogstagram_id = dogstagram.id" +
            "                 ORDER BY created_at DESC" +
            "                 LIMIT 1)) AS last_liked_nickname," +
            "    dogstagram.created_at AS created_at," +
            "    (SELECT COUNT(*)" +
            "     FROM dogstagram_like m" +
            "     WHERE m.dogstagram_id = dogstagram.id) AS total_like," +
            "   CASE  WHEN EXISTS (SELECT 1" +
            "                     FROM dogstagram_like dl" +
            "                     WHERE dl.dogstagram_id = dogstagram.id AND dl.user_id = :user_id" +
            "                 ) THEN TRUE" +
            "                 ELSE FALSE" +
            "                 END AS is_liked " +
            " FROM" +
            "    dogstagram" +
            "        INNER JOIN member ON member.id = dogstagram.member_id" +
            "        INNER JOIN attachment ON dogstagram.attachment_id = attachment.id" +
            " WHERE dog_type ~* :search_word " +
            "ORDER BY" +
            "    dogstagram.created_at DESC " +
            "LIMIT :take " +
            "OFFSET :skip", nativeQuery = true)
    public List<IDogstagram> searchDogstagramBy(
            @Param(value = "user_id") Long userId,
            @Param(value = "search_word") String searchWord,
            @Param(value = "take") int take,
            @Param(value = "skip") int skip);
}
