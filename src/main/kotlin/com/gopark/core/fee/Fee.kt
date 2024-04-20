package com.gopark.core.fee

import com.gopark.core.vehicle_type.VehicleType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.OffsetDateTime
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener


@Entity
@Table(name = "Fees")
@EntityListeners(AuditingEntityListener::class)
class Fee {

    @Id
    @Column(
        nullable = false,
        updatable = false
    )
    @SequenceGenerator(
        name = "primary_sequence",
        sequenceName = "primary_sequence",
        allocationSize = 1,
        initialValue = 10000
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "primary_sequence"
    )
    var feeId: Int? = null

    @Column(
        precision = 12,
        scale = 2
    )
    var amount: BigDecimal? = null

    @Column(length = 50)
    var duration: String? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "vehicle_type_id",
        nullable = false
    )
    var vehicleType: VehicleType? = null

    @CreatedDate
    @Column(
        nullable = false,
        updatable = false
    )
    var dateCreated: OffsetDateTime? = null

    @LastModifiedDate
    @Column(nullable = false)
    var lastUpdated: OffsetDateTime? = null

}
