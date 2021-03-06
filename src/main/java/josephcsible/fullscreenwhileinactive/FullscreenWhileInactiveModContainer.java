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

import com.google.common.eventbus.EventBus;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;

public class FullscreenWhileInactiveModContainer extends DummyModContainer {
	public FullscreenWhileInactiveModContainer() {
		super(new ModMetadata());
		ModMetadata metadata = getMetadata();
		// XXX almost all this is duplicated between here and mcmod.info
		metadata.modId = "fullscreenwhileinactive";
		// XXX version is duplicated between here and build.gradle
		metadata.version = "1.0.0";
		metadata.name = "FullscreenWhileInactive";
		metadata.description = "Allows Minecraft to stay fullscreen when it becomes inactive, also fixing bug MC-55045";
		metadata.url = "http://minecraft.curseforge.com/projects/fullscreenwhileinactive";
		metadata.authorList.add("Joseph C. Sible");
	}

	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		return true; // even if we don't have anything to register for, if we return false, Forge says we're not loaded
	}
}
