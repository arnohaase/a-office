package com.ajjpj.aoffice.text

import com.ajjpj.aoffice.util.Size


class FontId(val id: String) extends AnyVal

//TODO where to put 'language'? Probably TextFormat, usually falling through to document / system default? This allows overriding for single words

case class TextFormat(font: FontId, fontSize: Size, italics: Boolean) //TODO weight, underline, ...

class TextFormatTemplateId(val id: String) extends AnyVal
trait TextFormatTemplateRegistry {
  def resolve(templateId: TextFormatTemplateId): TextFormatOverrides
}

case class TextFormatOverrides(font: Option[FontId] = Option.empty, fontSize: Option[Size] = Option.empty,
                               italics: Option[Boolean] = Option.empty
                              ) {
  def merge (format: TextFormat) = TextFormat(
    font.getOrElse(format.font),
    fontSize.getOrElse(format.fontSize),
    italics.getOrElse(format.italics)
  )
}
object TextFormatOverrides {
  final val None = TextFormatOverrides()
}

sealed trait ParagraphAlignment
object ParagraphAlignment {
  case object Left extends ParagraphAlignment
  case object Center extends ParagraphAlignment
  case object Right extends ParagraphAlignment
  case object Justified extends ParagraphAlignment
}

//TODO variants of line spacing: absolute, multiple of line height, ...
//TODO angle
case class ParagraphFormat(lineSpacing: Size, alignment: ParagraphAlignment,
                           defaultTextFormat: TextFormat)

class ParagraphFormatTemplateId(val id: String) extends AnyVal
trait ParagraphFormatTemplateRegistry {
  def systemDefault: ParagraphFormat
  def resolve(templateId: ParagraphFormatTemplateId): ParagraphFormat
}

case class ParagraphFormatOverrides(lineSpacing: Option[Size], alignment: Option[ParagraphAlignment]) {
  def merge (format: ParagraphFormat) =
    ParagraphFormat(
      lineSpacing.getOrElse(format.lineSpacing),
      alignment.getOrElse(format.alignment),
      format.defaultTextFormat
    )
}
