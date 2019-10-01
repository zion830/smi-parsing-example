package smi_parser

import java.io.*
import javax.swing.JOptionPane.showMessageDialog

object FileUtil {

    fun getFileContents(file: File): ArrayList<String> {
        val result = arrayListOf<String>()
        var startAdd = false

        try {
            val reader = BufferedReader(FileReader(file))
            var line: String? = reader.readLine()

            while (line != null && line != "</BODY>") {
                if (line == "<BODY>") {
                    startAdd = true
                }

                if (startAdd) {
                    result.add(line)
                }

                line = reader.readLine()
            }
        } catch (ex: FileNotFoundException) {
            showMessageDialog(null, "존재하지 않는 파일입니다. 파일을 다시 선택해주세요.")
        } catch (ex: IOException) {
            showMessageDialog(null, "오류가 발생했습니다. 다시 시도해주세요.")
        }

        return result
    }
}