package com.ajjpj.aoffice.text


sealed trait DocAtom //TODO unseal this and use IDs and a registry?
case class DocTextAtom(text: String, formatTemplate: TextFormatTemplateId, formatOverrides: TextFormatOverrides) extends DocAtom
//TODO other text atoms - author, pageNum, lastPrintDate, ...; \cite; inlineImage; ...

sealed trait DocPart //TODO unseal this and use IDs and a registry?
case class DocParagraph(atoms: Vector[DocAtom], formatTemplate: ParagraphFormatTemplateId, formatOverrides: ParagraphFormatOverrides) extends DocPart

//TODO tables, images, formulas

case class DocFlow(parts: Vector[DocPart])
