package smi_parser

import java.awt.BorderLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.io.File
import javax.swing.*
import javax.swing.filechooser.FileNameExtensionFilter


class MainFrame : JFrame("smi 파싱"), ActionListener {
    private val addFileBtn = JButton("자막 파일 선택하기")
    private val fileNameLabel = JLabel("smi 확장자의 자막 파일을 선택한 후 '자막 분석 시작' 버튼을 클릭해주세요.")
    private val startExtractBtn = JButton("자막 분석 시작")
    private val contentTextArea = JTextArea(30, 35)
    private var selectedFile: File? = null

    init {
        setSize(500, 610)
        setLocationRelativeTo(null)
    }

    internal fun createPanel() {
        val mainPane = MainFrame()
        val jPanel = JPanel()

        val scrollPane = JScrollPane(
            contentTextArea,
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
        )

        addFileBtn.addActionListener(this)
        startExtractBtn.addActionListener(this)
        contentTextArea.isEditable = false

        jPanel.add(fileNameLabel)
        jPanel.add(addFileBtn)
        jPanel.add(startExtractBtn)
        jPanel.add(scrollPane)

        mainPane.apply {
            add(jPanel, BorderLayout.CENTER)
            isVisible = true
            defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        }
    }

    override fun actionPerformed(e: ActionEvent) {
        val chooser = JFileChooser()

        when (e.source as JButton) {
            addFileBtn -> {
                chooser.apply {
                    dialogTitle = "파일 열기"
                    fileFilter = FileNameExtensionFilter(".smi", "smi")
                }

                val result = chooser.showOpenDialog(null)
                fileNameLabel.text = "선택된 파일 : ${chooser.selectedFile.path}"

                if (result == JFileChooser.APPROVE_OPTION) {
                    selectedFile = chooser.selectedFile
                }
            }
            startExtractBtn -> {
                selectedFile?.let {
                    val content = FileUtil.getFileContents(selectedFile!!)
                    val subtitles = StringUtil.parseSubtitle(content)

                    contentTextArea.text = StringUtil.getDisplayString(subtitles)
                }
            }
        }
    }
}