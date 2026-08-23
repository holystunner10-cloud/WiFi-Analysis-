package com.example.data.repository

import com.example.data.model.VirtualFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class VirtualFileSystemRepository {

    private val initialFiles = mutableListOf(
        // Folders
        VirtualFile(path = "C:", name = "C:", isDirectory = true),
        VirtualFile(path = "C:\\Users", name = "Users", isDirectory = true),
        VirtualFile(path = "C:\\Users\\Admin", name = "Admin", isDirectory = true),
        VirtualFile(path = "C:\\Users\\Admin\\Desktop", name = "Desktop", isDirectory = true),
        VirtualFile(path = "C:\\Users\\Admin\\Documents", name = "Documents", isDirectory = true),
        VirtualFile(path = "C:\\Users\\Admin\\Downloads", name = "Downloads", isDirectory = true),
        VirtualFile(path = "C:\\Users\\Admin\\Projects", name = "Projects", isDirectory = true),
        VirtualFile(path = "C:\\Users\\Admin\\Pictures", name = "Pictures", isDirectory = true),

        // Files
        VirtualFile(
            path = "C:\\Users\\Admin\\Desktop\\Readme.txt",
            name = "Readme.txt",
            isDirectory = false,
            content = "========================================\n" +
                    "WELCOME TO PC DESKTOP FOR ANDROID\n" +
                    "========================================\n\n" +
                    "Your portable desktop computing environment is ready!\n\n" +
                    "Key Highlights:\n" +
                    "1. Multi-Window Compositor: Move, resize, minimize, maximize windows seamlessly.\n" +
                    "2. PC App Store: Install developer tools, web apps, and utilities in 1-click.\n" +
                    "3. Gemini Copilot AI: Your smart assistant in the taskbar and terminal.\n" +
                    "4. Code Studio IDE: Write and test Python & JavaScript scripts.\n" +
                    "5. Interactive Terminal: Full CLI with 'help', 'matrix', 'neofetch', and 'gemini' commands.\n" +
                    "6. Android Apps Integration: Launch real device apps from Start Menu!\n\n" +
                    "Enjoy your desktop experience!",
            sizeBytes = 780,
            extension = "txt"
        ),
        VirtualFile(
            path = "C:\\Users\\Admin\\Documents\\Project_Ideas.md",
            name = "Project_Ideas.md",
            isDirectory = false,
            content = "# 🚀 2026 Developer Roadmap\n\n" +
                    "## High Priority Projects\n" +
                    "- [x] Design Android PC Desktop Launcher with Material 3\n" +
                    "- [x] Integrate Gemini Copilot AI Desktop Assistant\n" +
                    "- [ ] Build cloud sync bridge for virtual filesystem\n" +
                    "- [ ] Connect custom Bluetooth keyboard & mouse bindings\n\n" +
                    "## Notes\n" +
                    "* Use Code Studio for rapid prototyping.\n" +
                    "* Test CLI scripts using the Terminal app.",
            sizeBytes = 420,
            extension = "md"
        ),
        VirtualFile(
            path = "C:\\Users\\Admin\\Projects\\hello_world.py",
            name = "hello_world.py",
            isDirectory = false,
            content = "# Python Demo for Code Studio\n" +
                    "def main():\n" +
                    "    print('Initializing Android PC Desktop OS...')\n" +
                    "    apps = ['Gemini Copilot', 'Code Studio', 'Terminal', 'File Explorer']\n" +
                    "    for index, app in enumerate(apps, 1):\n" +
                    "        print(f'[{index}] {app} active & ready.')\n" +
                    "    print('All systems running smoothly!')\n\n" +
                    "if __name__ == '__main__':\n" +
                    "    main()\n",
            sizeBytes = 385,
            extension = "py"
        ),
        VirtualFile(
            path = "C:\\Users\\Admin\\Projects\\index.html",
            name = "index.html",
            isDirectory = false,
            content = "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "  <title>My Web Project</title>\n" +
                    "  <style>\n" +
                    "    body { font-family: sans-serif; background: #0b132b; color: #fff; text-align: center; padding: 40px; }\n" +
                    "    h1 { color: #60a5fa; }\n" +
                    "  </style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "  <h1>Hello from PC Desktop!</h1>\n" +
                    "  <p>Built with Jetpack Compose & Kotlin.</p>\n" +
                    "</body>\n" +
                    "</html>",
            sizeBytes = 390,
            extension = "html"
        )
    )

    private val _files = MutableStateFlow<List<VirtualFile>>(initialFiles)
    val files: StateFlow<List<VirtualFile>> = _files.asStateFlow()

    fun listFiles(parentPath: String): List<VirtualFile> {
        val normalizedParent = parentPath.trimEnd('\\')
        return _files.value.filter { file ->
            if (file.path == normalizedParent) return@filter false
            val fileParent = file.path.substringBeforeLast('\\', "")
            fileParent.equals(normalizedParent, ignoreCase = true)
        }.sortedWith(compareByDescending<VirtualFile> { it.isDirectory }.thenBy { it.name.lowercase() })
    }

    fun getFile(path: String): VirtualFile? {
        return _files.value.find { it.path.equals(path, ignoreCase = true) }
    }

    fun saveFileContent(path: String, content: String) {
        val existing = getFile(path)
        if (existing != null) {
            _files.value = _files.value.map {
                if (it.path.equals(path, ignoreCase = true)) {
                    it.copy(
                        content = content,
                        sizeBytes = content.toByteArray().size.toLong(),
                        lastModified = System.currentTimeMillis()
                    )
                } else it
            }
        } else {
            val name = path.substringAfterLast('\\')
            val ext = name.substringAfterLast('.', "")
            val newFile = VirtualFile(
                path = path,
                name = name,
                isDirectory = false,
                content = content,
                sizeBytes = content.toByteArray().size.toLong(),
                lastModified = System.currentTimeMillis(),
                extension = ext
            )
            _files.value = _files.value + newFile
        }
    }

    fun createFile(parentPath: String, name: String, isDirectory: Boolean = false, content: String = ""): Boolean {
        val normalizedParent = parentPath.trimEnd('\\')
        val fullPath = "$normalizedParent\\$name"
        if (getFile(fullPath) != null) return false

        val ext = if (isDirectory) "" else name.substringAfterLast('.', "")
        val newFile = VirtualFile(
            path = fullPath,
            name = name,
            isDirectory = isDirectory,
            content = content,
            sizeBytes = if (isDirectory) 0 else content.toByteArray().size.toLong(),
            lastModified = System.currentTimeMillis(),
            extension = ext
        )
        _files.value = _files.value + newFile
        return true
    }

    fun deleteFile(path: String): Boolean {
        if (path == "C:" || path == "C:\\Users" || path == "C:\\Users\\Admin") return false
        _files.value = _files.value.filterNot { it.path.startsWith(path, ignoreCase = true) }
        return true
    }

    fun getUsedStorageKb(): Long {
        return _files.value.sumOf { it.sizeBytes } / 1024
    }
}
