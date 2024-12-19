package spring.sample.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Component;
import spring.sample.model.RoutePlanJob;

@Slf4j
@Component
@RequiredArgsConstructor
public class DbService {

    private final Jdbi jdbi;

    public static List<RoutePlanJob> peekJobs(@NonNull final Handle handle, final String status, final Integer limit) {
        log.info("RoutePlanJobRepo.dequeueJobs, statuses: {}, limit: {}", status, limit);
        if (Objects.nonNull(status) && Objects.nonNull(limit)) {
            final int rowLimit = limit > 0 ? limit : 1;
            final String sql = "select * from or_route_plan_jobs r where r.status = :status and r.attempts < :attempts order by r.attempts desc, r.id asc limit :limit";
            final List<RoutePlanJob> jobs = handle.createQuery(sql)
                .bind("status", status)
                .bind("attempts", RoutePlanJob.attemptLimit)
                .bind("limit", rowLimit)
                .map(r -> RoutePlanJob.builder()
                    .id(r.getColumn("id", Integer.class))
                    .jobType(r.getColumn("job_type", String.class))
                    .planId(r.getColumn("plan_id", Long.class))
                    .jobId(r.getColumn("job_id", String.class))
                    .routeId(r.getColumn("route_id", String.class))
                    .request(r.getColumn("request", String.class))
                    .response(r.getColumn("response", String.class))
                    .status(r.getColumn("status", String.class))
                    .attempts(r.getColumn("attempts", Integer.class))
                    .actionUser(r.getColumn("action_user", Long.class))
                    .attemptTime(r.getColumn("attempt_time", LocalDateTime.class))
                    .requestTime(r.getColumn("request_time", LocalDateTime.class))
                    .respondTime(r.getColumn("respond_time", LocalDateTime.class))
                    .createTime(r.getColumn("create_time", LocalDateTime.class))
                    .build())
                .collectIntoList();
            return jobs;
        }
        return List.of();
    }

    public List<RoutePlanJob> peekJobs(final String status, final Integer limit) {
        return jdbi.withHandle(handle -> peekJobs(handle, status, limit));
    }


}
