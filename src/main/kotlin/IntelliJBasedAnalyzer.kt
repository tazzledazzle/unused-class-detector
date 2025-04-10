package com.tazzledazzle

import com.intellij.analysis.AnalysisScope
import com.intellij.codeInspection.InspectionEngine
import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.deadCode.UnusedDeclarationInspection
import com.intellij.codeInspection.ex.GlobalInspectionContextImpl
import com.intellij.ide.impl.ProjectUtil
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ReadAction
import com.intellij.openapi.project.ProjectManager
import com.intellij.psi.PsiClass


class IntelliJBasedAnalyzer(private val projectPath: String) {

    fun analyzeProject(projectPath: String): Set<String> {
        val unusedClasses = mutableSetOf<String>()
        val project = ProjectUtil.openOrImport(projectPath, null, false)
            ?: throw IllegalArgumentException("Failed to open project at path: $projectPath")
        try {
//            ApplicationManager.getApplication().invokeAndWait {
//                val inspectionManager = InspectionManager.getInstance(project)
//                val analysisScope = AnalysisScope(project)
//
//                // Depending on your IntelliJ Platform version,
//                // consider using GlobalInspectionContextBase instead if needed.
//                val inspectionContext = GlobalInspectionContextImpl(inspectionManager, analysisScope)
//
//                ReadAction.run<RuntimeException> {
//                    val inspection = UnusedDeclarationInspection(true)
//                    val problems = InspectionEngine.runInspectionOnScope(
//                        analysisScope,
//                        inspection,
//                        inspectionContext,
//                        null
//                    )
//
//                    problems.forEach { problem ->
//                        (problem.psiElement as? PsiClass)?.qualifiedName?.let { qualifiedName ->
//                            unusedClasses.add(qualifiedName)
//                        }
//                    }
//                }
//            }
        } catch (e: Exception) {
            println("Error during analysis: ${e.message}")
            e.printStackTrace()
        } finally {
            ProjectManager.getInstance().closeProject(project)
        }
        return unusedClasses
    }
}

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Usage: <program> <path-to-intellij-project>")
        return
    }

    val analyzer = IntelliJBasedAnalyzer(args[0])
//    val unusedClasses = analyzer.analyzeProject()

    println("Unused classes found by IntelliJ analysis:")
//    unusedClasses.forEach(::println)
}

