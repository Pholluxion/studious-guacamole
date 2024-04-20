package com.gopark.core.spot

import com.gopark.core.parking.Parking
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import java.time.OffsetDateTime
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener


@Entity
@Table(name = "Spots")
@EntityListeners(AuditingEntityListener::class)
class Spot {

    @Id
    @Column(
        nullable = false,
        updatable = false
    )
    @SequenceGenerator(
        name = "spot_sequence",
        sequenceName = "spot_sequence",
        allocationSize = 1,
        initialValue = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "spot_sequence"
    )
    var spotId: Int? = null

    @Column(length = 20)
    var licensePlate: String? = null

    @Column
    @Enumerated(EnumType.STRING)
    var paymentStatus: PaymentStatus? = null

    @Column
    var arrivalTime: OffsetDateTime? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "parking_id",
        nullable = false
    )
    var parking: Parking? = null

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
