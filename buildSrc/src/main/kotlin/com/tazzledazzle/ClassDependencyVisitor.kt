package com.tazzledazzle

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.FieldVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type

class ClassDependencyVisitor(private val usedClasses: MutableSet<String>) : ClassVisitor(Opcodes.ASM9) {
    private var currentClass: String = ""

    override fun visit(
        version: Int,
        access: Int,
        name: String,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        currentClass = name.replace('/', '.')
        usedClasses.add(currentClass)

        superName?.let {
            usedClasses.add(it.replace('/', '.'))
        }

        interfaces?.forEach {
            usedClasses.add(it.replace('/', '.'))
        }

        //todo: this might need to be removed
        super.visit(version, access, name, signature, superName, interfaces)
    }

    override fun visitField(
        access: Int,
        name: String,
        descriptor: String,
        signature: String?,
        value: Any?
    ): FieldVisitor? {
        addClassFromType(Type.getType(descriptor), usedClasses)
        return null
    }
    override fun visitMethod(
        access: Int,
        name: String,
        descriptor: String,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        // Add return type and parameter types to used classes
        val methodType = Type.getMethodType(descriptor)
        addClassFromType(methodType.returnType, usedClasses)

        for (paramType in methodType.argumentTypes) {
            addClassFromType(paramType, usedClasses)
        }

        // Add exception types to used classes
        exceptions?.forEach {
            usedClasses.add(it.replace('/', '.'))
        }

        return MethodDependencyVisitor(usedClasses)
    }

//    private fun addClassFromType(type: Type) {
//        when (type.sort) {
//            Type.OBJECT -> usedClasses.add(type.className)
//            Type.ARRAY -> {
//                var elementType = type
//                while (elementType.sort == Type.ARRAY) {
//                    elementType = elementType.elementType
//                }
//                if (elementType.sort == Type.OBJECT) {
//                    usedClasses.add(elementType.className)
//                }
//            }
//        }
//    }
}

class MethodDependencyVisitor(
    private val usedClasses: MutableSet<String>
) : MethodVisitor(Opcodes.ASM9) {
    override fun visitMethodInsn(
        opcode: Int,
        owner: String,
        name: String,
        descriptor: String,
        isInterface: Boolean
    ) {
        // Add the class containing the method
        usedClasses.add(owner.replace('/', '.'))

        // Add return type and parameter types
        val methodType = Type.getMethodType(descriptor)
        addClassFromType(methodType.returnType, usedClasses)

        for (paramType in methodType.argumentTypes) {
            addClassFromType(paramType, usedClasses)
        }
    }

    override fun visitTypeInsn(opcode: Int, type: String) {
        // Add class used in instructions like NEW, INSTANCEOF, etc.
        usedClasses.add(type.replace('/', '.'))
    }

    override fun visitFieldInsn(opcode: Int, owner: String, name: String, descriptor: String) {
        // Add field owner class and field type
        usedClasses.add(owner.replace('/', '.'))
        addClassFromType(Type.getType(descriptor), usedClasses)
    }


}

private fun addClassFromType(type: Type, usedClasses: MutableSet<String>) {
    when (type.sort) {
        Type.OBJECT -> usedClasses.add(type.className)
        Type.ARRAY -> {
            var elementType = type
            while (elementType.sort == Type.ARRAY) {
                elementType = elementType.elementType
            }
            if (elementType.sort == Type.OBJECT) {
                usedClasses.add(elementType.className)
            }
        }
    }
}