package com.skt.ems.formbuilder.repository;

import com.skt.ems.formbuilder.entity.Form;
import com.skt.ems.formbuilder.enums.FormStatus;
import com.skt.ems.formbuilder.enums.FormType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormRepository extends JpaRepository<Form, String> {
    Optional<Form> findByName(String name);

    Page<Form> findByTenantIdAndEventId(String tenantId, String eventId, Pageable pageable);

    Optional<Form> findByCohortAndEventId(String cohort, String eventId);

    Optional<Form> findByCohortAndEventIdAndFormType(String cohort, String eventId, FormType formType);

    Page<Form> findByTenantIdAndEventIdAndFormType(String tenantId, String eventId, FormType formType, Pageable pageable);

    Page<Form> findByTenantIdAndEventIdAndFormStatus(String tenantId, String eventId, FormStatus formStatus, Pageable pageable);

    Page<Form> findByTenantIdAndEventIdAndFormTypeAndFormStatus(String tenantId, String eventId, FormType formType, FormStatus formStatus, Pageable pageable);

    @Query(value = """
    SELECT f.*
    FROM forms f
    WHERE f.tenant_id = :tenantId
      AND f.event_id = :eventId
      AND (:status IS NULL OR f.form_status = :status)
      AND (:formType IS NULL OR f.form_type = :formType)
      AND (:cohort IS NULL OR :cohort = '' OR f.cohort = :cohort)
    """,
            countQuery = """
    SELECT COUNT(1)
    FROM forms f
    WHERE f.tenant_id = :tenantId
      AND f.event_id = :eventId
      AND (:status IS NULL OR f.form_status = :status)
      AND (:formType IS NULL OR f.form_type = :formType)
      AND (:cohort IS NULL OR :cohort = '' OR f.cohort = :cohort)
    """,
            nativeQuery = true)
    Page<Form> findFilteredForms(
            @Param("tenantId") String tenantId,
            @Param("eventId") String eventId,
            @Param("status") String status,      // pass FormStatus.PUBLISHED.name() or null
            @Param("formType") String formType,  // pass FormType.name() or null
            @Param("cohort") String cohort,      // pass cohort or null
            Pageable pageable
    );

    @Query(value = """
    SELECT
        f.id          AS formId,
        f.name        AS formName,
        f.cohort      AS cohort,
        f.form_type   AS formType,
        COUNT(fr.id)  AS responseCount
    FROM forms f
    LEFT JOIN form_responses fr ON fr.form_id = f.id
    WHERE f.event_id = :eventId
      AND f.form_status = 'PUBLISHED'
      AND (:formType IS NULL OR f.form_type = :formType)
    GROUP BY f.id, f.name, f.cohort, f.form_type
    ORDER BY f.created_at DESC
    """, nativeQuery = true)
    List<Object[]> getPublishedFormWiseResponseCounts(
            @Param("eventId") String eventId,
            @Param("formType") String formType
    );



    @Query(value = """
    SELECT COUNT(*)
    FROM forms f
    WHERE f.event_id = :eventId
      AND f.form_status = 'PUBLISHED'
      AND (:formType IS NULL OR f.form_type = :formType)
    """, nativeQuery = true)
    Long countPublishedForms(@Param("eventId") String eventId, @Param("formType") String formType);


    @Query(value = """
    SELECT COUNT(fr.id)
    FROM form_responses fr
    INNER JOIN forms f ON f.id = fr.form_id
    WHERE f.event_id = :eventId
      AND f.form_status = 'PUBLISHED'
      AND (:formType IS NULL OR f.form_type = :formType)
    """, nativeQuery = true)
    Long countTotalResponsesForPublishedForms(@Param("eventId") String eventId, @Param("formType") String formType);

}
