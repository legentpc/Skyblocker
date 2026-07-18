package de.hysky.skyblocker.skyblock.accessories;

import de.hysky.skyblocker.config.SkyblockerConfigManager;
import de.hysky.skyblocker.utils.container.SimpleContainerSolver;
import de.hysky.skyblocker.utils.render.gui.ColorHighlight;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.CommonColors;
import net.minecraft.util.ARGB;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AccessoriesContainerSolver extends SimpleContainerSolver {
	private static final int COLOR = ARGB.color(0.7f, CommonColors.GREEN);
	public static final AccessoriesContainerSolver INSTANCE = new AccessoriesContainerSolver();

	@Nullable String highlightedAccessory;

	protected AccessoriesContainerSolver() {
		super(AccessoriesHelper.ACCESSORY_BAG_TITLE);
	}

	@Override
	public List<ColorHighlight> getColors(Int2ObjectMap<ItemStack> slots) {
		List<ColorHighlight> highlights = new ArrayList<>();
		Object2IntOpenHashMap<String> counts = new Object2IntOpenHashMap<>();
		Set<String> otherPageIds = AccessoriesHelper.getCollectedAccessoryIdsOnOtherPages();

		for (Int2ObjectMap.Entry<ItemStack> entry : slots.int2ObjectEntrySet()) {
			String id = entry.getValue().getSkyblockId();
			if (!id.isEmpty()) {
				counts.addTo(id, 1);
			}
		}

		for (Int2ObjectMap.Entry<ItemStack> entry : slots.int2ObjectEntrySet()) {
			String id = entry.getValue().getSkyblockId();
			if (!id.isEmpty() && (counts.getInt(id) > 1 || otherPageIds.contains(id))) {
				highlights.add(ColorHighlight.red(entry.getIntKey()));
			}
		}

		if (highlightedAccessory != null) {
			for (Int2ObjectMap.Entry<ItemStack> entry : slots.int2ObjectEntrySet()) {
				if (entry.getValue().getSkyblockId().equals(highlightedAccessory)) {
					highlights.add(new ColorHighlight(entry.getIntKey(), COLOR));
				}
			}
		}

		return highlights;
	}

	@Override
	public boolean isEnabled() {
		return SkyblockerConfigManager.get().helpers.enableAccessoriesHelperWidget
				&& SkyblockerConfigManager.get().helpers.enableDuplicateAccessoryHighlight;
	}
}
