package ru.practicum.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.RequestOutDTO;
import ru.practicum.model.Request;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {

    @Query(value = "SELECT new ru.practicum.RequestOutDTO(a.name, r.uri, COUNT(r.ip)) " +
            "FROM Request as r " +
            "LEFT JOIN Application as a ON a.id = r.app.id " +
            "WHERE r.timestamp between ?1 AND ?2 " +
            "AND r.uri IN (?3) " +
            "GROUP BY a.name, r.uri " +
            "ORDER BY COUNT(r.ip) DESC ")
    List<RequestOutDTO> getAllRequestsWithUri(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(value = "SELECT new ru.practicum.RequestOutDTO(a.name, r.uri, COUNT(DISTINCT r.ip)) " +
            "FROM Request as r " +
            "LEFT JOIN Application as a ON a.id = r.app.id " +
            "WHERE r.timestamp between ?1 AND ?2 " +
            "AND r.uri IN (?3) " +
            "GROUP BY a.name, r.uri " +
            "ORDER BY COUNT(DISTINCT r.ip) DESC ")
    List<RequestOutDTO> getUniqueIpRequestsWithUri(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(value = "SELECT new ru.practicum.RequestOutDTO(a.name, r.uri, COUNT(DISTINCT r.ip)) " +
            "FROM Request as r " +
            "LEFT JOIN Application as a ON a.id = r.app.id " +
            "WHERE r.timestamp between ?1 AND ?2 " +
            "GROUP BY a.name, r.uri " +
            "ORDER BY COUNT(DISTINCT r.ip) DESC ")
    List<RequestOutDTO> getUniqueIpRequestsWithoutUri(LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT new ru.practicum.RequestOutDTO(a.name, r.uri, COUNT(r.ip)) " +
            "FROM Request as r " +
            "LEFT JOIN Application as a ON a.id = r.app.id " +
            "WHERE r.timestamp between ?1 AND ?2 " +
            "GROUP BY a.name, r.uri " +
            "ORDER BY COUNT(r.ip) DESC ")
    List<RequestOutDTO> getAllRequestsWithoutUri(LocalDateTime start, LocalDateTime end);
}
