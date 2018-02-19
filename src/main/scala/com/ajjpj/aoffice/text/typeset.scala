package com.ajjpj.aoffice.text

import com.ajjpj.aoffice.util.Size



sealed trait TypeSetAtom {
  def moveY (dy: Size): TypeSetAtom
} //TODO unseal this and use IDs and a registry?
/**
  * @param posY top-to-bottom offset of the baseline
  */
case class TypeSetTextAtom(posX: Size, posY: Size, text: String, format: TextFormat) extends TypeSetAtom {
  override def moveY (dy: Size) = copy(posY = posY + dy)
}


//----- line typesetting

case class TypeSetLine (height: Size, width: Size, atoms: Vector[TypeSetAtom])

case class TypeSetLineResult(line: TypeSetLine, remainingAtoms: Vector[DocAtom])
trait LineTypeSetter {
  /**
    * This method is the heart of type setting, and it is required to do tons of advanced stuff. This includes, but
    *  is not limited to:
    *  <ul>
    *    <li> hyphenation, and adding hyphens as 'synthetic' atoms (or as text to existing atoms)
    *    <li> translating alignment into actual positions, which means breaking atoms into separate words for justified
    *          alignment
    *    <li> inset of a paragraph's first line
    *  </ul>
    */
  def typeSetLine(atoms: Vector[DocAtom], maxWidth: Size, paragraphFormat: ParagraphFormat, lineNumberInParagraph: Int): TypeSetLineResult
}
//TODO hyphenation of a single word spanning more than two lines

//----- flows

case class TypeSetFrame(atoms: Vector[TypeSetAtom])
case class TypeSetFlow(frames: Vector[TypeSetFrame])

//TODO vertical alignment
class SingleRectFlow(width: Size, height: Size) {
  def typeSetFlow(doc: DocFlow, lineTypeSetter: LineTypeSetter, paragraphFormatTemplateRegistry: ParagraphFormatTemplateRegistry): TypeSetFlow = {
    var elements = Vector.empty[TypeSetAtom]
    var totalHeight = Size.Zero

    doc.parts.foreach {
      case DocParagraph(atoms, formatTemplate, formatOverrides) =>
        val format = formatOverrides.merge(paragraphFormatTemplateRegistry.resolve(formatTemplate))

        var lineNumberInParagraph = 0
        var remaining = atoms
        while(remaining.nonEmpty) {
          val typeSetLineResult = lineTypeSetter.typeSetLine(atoms, width, format, lineNumberInParagraph)
          lineNumberInParagraph += 1
          remaining = typeSetLineResult.remainingAtoms

          elements ++= typeSetLineResult.line.atoms.map(_.moveY(totalHeight))

          totalHeight += typeSetLineResult.line.height
          //TODO line spacing
        }
        //TODO paragraph spacing
    }

    TypeSetFlow(Vector(TypeSetFrame(elements)))
  }
}

//TODO MultiRectFlow

//TODO line width may vary.

//TODO cyclic width / height, separate first N / last N
//TODO ChapterPageFlow - separate variable-width flow for chapter title --> concept of page




// rendering



//TODO first letter big, across several lines
//TODO footnotes, relatively positioned images --> separate but on the same page as something else