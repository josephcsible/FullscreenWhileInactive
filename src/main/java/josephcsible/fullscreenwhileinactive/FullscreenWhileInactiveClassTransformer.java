/*
FullscreenWhileInactive Minecraft Mod
Copyright (C) 2016 Joseph C. Sible

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License along
with this program; if not, write to the Free Software Foundation, Inc.,
51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*/

package josephcsible.fullscreenwhileinactive;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import static org.objectweb.asm.Opcodes.*;

import net.minecraft.launchwrapper.IClassTransformer;

public class FullscreenWhileInactiveClassTransformer implements IClassTransformer {

	private void transformRunGameLoop(MethodNode mn) {
		/*
		We're trying to remove this:
		if (!Display.isActive() && this.fullscreen)
		{
			this.toggleFullscreen();
		}

		Here's the relevant piece of the bytecode:
		L43 *** target node
		LINENUMBER 1075 L43
		INVOKESTATIC org/lwjgl/opengl/Display.isActive ()Z *** searched for
		IFNE L44
		ALOAD 0
		GETFIELD net/minecraft/client/Minecraft.fullscreen : Z
		IFEQ L44
		L45
		LINENUMBER 1077 L45
		ALOAD 0
		INVOKEVIRTUAL net/minecraft/client/Minecraft.toggleFullscreen ()V
		*/

		AbstractInsnNode targetNode = null;
		for (AbstractInsnNode ain : mn.instructions.toArray())
		{
			if (!(ain instanceof MethodInsnNode)) {
				continue;
			}
			MethodInsnNode min = (MethodInsnNode) ain;
			if (min.owner.equals("org/lwjgl/opengl/Display") && min.name.equals("isActive"))
			{
				targetNode = min.getPrevious().getPrevious();
				break;
			}
		}
		if (targetNode == null)
		{
			System.err.println("Failed to find the part of runGameLoop we need to patch!");
			return;
		}
		System.out.println("Patching runGameLoop");
		for(int i = 0; i < 10; ++i) {
			mn.instructions.remove(targetNode.getNext());
		}
		mn.instructions.remove(targetNode);
	}

	private static ClassNode byteArrayToClassNode(byte[] basicClass) {
		ClassNode cn = new ClassNode();
		ClassReader cr = new ClassReader(basicClass);
		cr.accept(cn, 0);
		return cn;
	}

	private static byte[] classNodeToByteArray(ClassNode cn) {
		ClassWriter cw = new ClassWriter(0);
		cn.accept(cw);
		return cw.toByteArray();
	}

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass)
	{
		if(!transformedName.equals("net.minecraft.client.Minecraft")) {
			return basicClass;
		}
		ClassNode cn = byteArrayToClassNode(basicClass);
		for(MethodNode mn : cn.methods) {
			if (mn.name.equals(FullscreenWhileInactiveLoadingPlugin.runtimeDeobfuscationEnabled ? "ak" : "runGameLoop") && mn.desc.equals("()V")) {
				transformRunGameLoop(mn);
				return classNodeToByteArray(cn);
			}
		}
		System.err.println("Failed to find the runGameLoop method!");
		return classNodeToByteArray(cn);
	}
}
