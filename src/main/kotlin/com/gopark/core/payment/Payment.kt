package com.gopark.core.payment

import com.gopark.core.fee.Fee
import com.gopark.core.spot.Spot
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
import org.springframework.data.jpa.domain.support.AuditingEntityListener


@Entity
@Table(name = "Payments")
@EntityListeners(AuditingEntityListener::class)
class Payment {

    @Id
    @Column(
        nullable = false,
        updatable = false
    )
    @SequenceGenerator(
        name = "payment_sequence",
        sequenceName = "payment_sequence",
        allocationSize = 1,
        initialValue = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "payment_sequence"
    )
    var paymentId: Int? = null

    @Column(
        precision = 12,
        scale = 2
    )
    var paidAmount: BigDecimal? = null

    @Column
    var paymentTime: OffsetDateTime? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "spot_id",
        nullable = false
    )
    var spot: Spot? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "fee_id",
        nullable = false
    )
    var fee: Fee? = null


}
