package org.example.bigdisplayproject.data.remote.dto.slider

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class SlideData(
    val id: Long,
    @SerialName("mediaContent")
    val mediaContent: @Polymorphic MediaContent,
    val important: Boolean,
    @Serializable(with = BooleanOrLongSerializer::class) val timeStart: BooleanOrLong,
    @Serializable(with = BooleanOrLongSerializer::class) val timeEnd: BooleanOrLong,
    val timer: Long,
    val heading: String,
    val text: String,
    val links: List<SlideLink>,
    val attachments: SlideAttachment,
    val sides: SlideSides,
    val keyValue: List<SlideKeyValue>?,
    val indexSlide: Long
)

// Обёртка для Boolean/Long
sealed class BooleanOrLong {
    data class Bool(val value: Boolean) : BooleanOrLong()
    data class Num(val value: Long) : BooleanOrLong()
}

// Кастомный сериализатор
object BooleanOrLongSerializer : KSerializer<BooleanOrLong> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("BooleanOrLong", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: BooleanOrLong) {
        when (value) {
            is BooleanOrLong.Bool -> encoder.encodeBoolean(value.value)
            is BooleanOrLong.Num -> encoder.encodeLong(value.value)
        }
    }

    override fun deserialize(decoder: Decoder): BooleanOrLong {
        return try {
            BooleanOrLong.Num(decoder.decodeLong()) // Пробуем Long
        } catch (e: Exception) {
            BooleanOrLong.Bool(decoder.decodeBoolean()) // Если не вышло — Boolean
        }
    }
}