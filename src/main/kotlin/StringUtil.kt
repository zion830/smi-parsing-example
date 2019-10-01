package smi_parser

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL
import kr.co.shineware.nlp.komoran.core.Komoran


object StringUtil {

    fun parseSubtitle(contents: ArrayList<String>): ArrayList<Subtitle> {
        val result = arrayListOf<Subtitle>()
        val regex = """<SYNC Start=(\d+)><P Class=([\d\w]+)>""".toRegex()
        var startNumber = -1
        var className = ""

        contents.forEach {
            if (regex.containsMatchIn(it)) {
                val matchResult = regex.find(it)!!.destructured
                startNumber = matchResult.component1().toInt()
                className = matchResult.component2()
            } else if (!it.startsWith("<") && !it.endsWith("&nbsp;")) {
                val content = it.replace("<br>", " ")
                result.add(Subtitle(startNumber, className, content))
            }
        }

        return result
    }

    fun getDisplayString(subtitles: ArrayList<Subtitle>): String {
        val komoran = Komoran(DEFAULT_MODEL.FULL)
        var displayStr = ""

        subtitles.forEach {
            val analyzeResult = komoran.analyze(it.content)
            val tokenList = analyzeResult.tokenList

            displayStr += "StartFrame : ${it.startNumber}" +
                    "\nClassName : ${it.className}" +
                    "\nOriginText : ${it.content}" +
                    "\n[AnalyzeResult]\n"

            for (token in tokenList) {
                displayStr += "(${token.beginIndex}, ${token.endIndex}, ${getWordClass(token.pos)}) ${token.morph}\n"
            }

            displayStr += "\n"
        }

        return displayStr
    }

    private fun getWordClass(type: String) = when (type) {
        "NNG", "NNP", "NNB" -> "명사"
        "NP", "NR" -> "대명사"
        "VV" -> "동사"
        "VA" -> "형용사"
        "VX" -> "보조용언"
        "VCP", "VCN" -> "지정사"
        "MM" -> "관형사"
        "MAG", "MAJ" -> "부사"
        "IC" -> "감탄사"
        "JKS", "JKC", "JKG", "JKO", "JKB", "JKV", "JKQ" -> "격조사"
        "JX" -> "보조사"
        "JC" -> "접속조사"
        "EP", "EF", "EC", "ETN", "ETM" -> "어미"
        "XPN" -> "접두사"
        "XSN", "XSV", "XSA" -> "접미사"
        "XR" -> "어근"
        "SF" -> "마침표,물음표,느낌표"
        "SP" -> "쉼표,가운뎃점,콜론,빗금"
        "SS" -> "따옴표,괄호표,줄표"
        "SE" -> "줄임표"
        "SO" -> "붙임표(물결,숨김,빠짐)"
        "SL" -> "외국어"
        "SH" -> "한자"
        "SW" -> "기타 기호"
        "NF" -> "명사추정범주"
        "NV" -> "용언추정범주"
        "SN" -> "숫자"
        else -> "분석불능범주"
    }
}