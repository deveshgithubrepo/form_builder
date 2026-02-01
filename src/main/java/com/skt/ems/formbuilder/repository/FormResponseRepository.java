package com.skt.ems.formbuilder.repository;

import com.skt.ems.formbuilder.entity.FormResponse;
import com.skt.ems.formbuilder.enums.FormType;
import com.skt.ems.formbuilder.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormResponseRepository extends JpaRepository<FormResponse, String> {
    Page<FormResponse> findByFormId(String formId, Pageable pageable);

    @Query("""
    SELECT fr
    FROM FormResponse fr, Form f
    WHERE fr.formId = f.id
      AND fr.urn = :urn
      AND (:formType IS NULL OR f.formType = :formType)
""")
    Page<FormResponse> findByUrnAndFormType(
            @Param("urn") String urn,
            @Param("formType") FormType formType,
            Pageable pageable
    );

    Optional<FormResponse> findByFormIdAndUsername(String formId, String username);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
                update FormResponse fr
                   set fr.formResponseStatus = :status,
                       fr.rejectReason = :rejectReason
                 where fr.id in :ids
            """)
    int bulkUpdateStatusAndReason(@Param("status") Status status,
                                  @Param("rejectReason") String rejectReason,
                                  @Param("ids") List<String> ids);

    List<FormResponse> findAllByIdIn(List<String> formResponseIds);


    @Query(
            value = """
        SELECT fr.*
        FROM form_responses fr
        WHERE fr.form_id = :formId

          AND ( :email IS NULL OR :email = ''
                OR LOWER(fr.username) LIKE CONCAT('%', LOWER(:email), '%') )

          AND ( :status IS NULL OR :status = ''
                OR fr.form_response_status = :status )

          AND ( :name IS NULL OR :name = ''
                OR LOWER(
                    CONCAT(
                      COALESCE(JSON_UNQUOTE(JSON_EXTRACT(fr.response, '$.personalInfo.firstName')), ''),
                      ' ',
                      COALESCE(JSON_UNQUOTE(JSON_EXTRACT(fr.response, '$.personalInfo.lastName')), ''),
                      ' ',
                      COALESCE(JSON_UNQUOTE(JSON_EXTRACT(fr.response, '$.personalInfo.name')), ''),
                      ' ',
                      COALESCE(JSON_UNQUOTE(JSON_EXTRACT(fr.response, '$.personalInfo.fullName')), '')
                    )
                  ) LIKE CONCAT('%', LOWER(:name), '%') )

        ORDER BY fr.created_at ASC
        """,
            countQuery = """
        SELECT COUNT(1)
        FROM form_responses fr
        WHERE fr.form_id = :formId
          AND ( :email IS NULL OR :email = ''
                OR LOWER(fr.username) LIKE CONCAT('%', LOWER(:email), '%') )
          AND ( :status IS NULL OR :status = ''
                OR fr.form_response_status = :status )
          AND ( :name IS NULL OR :name = ''
                OR LOWER(
                    CONCAT(
                      COALESCE(JSON_UNQUOTE(JSON_EXTRACT(fr.response, '$.personalInfo.firstName')), ''),
                      ' ',
                      COALESCE(JSON_UNQUOTE(JSON_EXTRACT(fr.response, '$.personalInfo.lastName')), ''),
                      ' ',
                      COALESCE(JSON_UNQUOTE(JSON_EXTRACT(fr.response, '$.personalInfo.name')), ''),
                      ' ',
                      COALESCE(JSON_UNQUOTE(JSON_EXTRACT(fr.response, '$.personalInfo.fullName')), '')
                    )
                  ) LIKE CONCAT('%', LOWER(:name), '%') )
        """,
            nativeQuery = true
    )
    Page<FormResponse> findByFormIdWithFilters(
            @Param("formId") String formId,
            @Param("email") String email,
            @Param("status") String status,
            @Param("name") String name,
            Pageable pageable
    );


    @Query(value = """
        SELECT fr.form_response_status AS status, COUNT(*) AS cnt
        FROM form_responses fr
        WHERE fr.form_id = :formId
        GROUP BY fr.form_response_status
        """, nativeQuery = true)
    List<Object[]> getStatusCountsByFormId(@Param("formId") String formId);
}
