
package com.goosepatrol.game.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.goosepatrol.game.GameViewport;
import com.goosepatrol.game.ecs.GameSystem;
import com.goosepatrol.game.ecs.GameWorld;

public class DecorationRenderSystem implements GameSystem {

    private static final float W = 1300f;
    private static final float H = 1000f;

    private final SpriteBatch batch = new SpriteBatch();
    private final GameViewport gameViewport;
    private final Texture sheet;

    private final TextureRegion tree1;
    private final TextureRegion tree2;
    private final TextureRegion tree3;
    private final TextureRegion tree4;

    private final TextureRegion bush1;
    private final TextureRegion bush2;
    private final TextureRegion bush3;
    private final TextureRegion bush4;

    private final TextureRegion flowers1;
    private final TextureRegion flowers2;
    private final TextureRegion flowers3;
    private final TextureRegion flowers4;

    private final TextureRegion bench;
    private final TextureRegion lamp;
    private final TextureRegion flowerBed;

    public DecorationRenderSystem(GameViewport gameViewport) {

        this.gameViewport = gameViewport;

        sheet = new Texture(
            Gdx.files.internal(
                "map/decorations/campus_decorations.png"
            )
        );

        sheet.setFilter(
            Texture.TextureFilter.Linear,
            Texture.TextureFilter.Linear
        );

        tree1 = crop(24, 36, 338, 358);
        tree2 = crop(394, 18, 382, 376);
        tree3 = crop(793, 34, 364, 363);
        tree4 = crop(1180, 35, 341, 363);

        bush1 = crop(46, 443, 249, 154);
        bush2 = crop(361, 444, 241, 153);
        bush3 = crop(653, 447, 253, 146);
        bush4 = crop(962, 451, 221, 141);

        flowers1 = crop(33, 639, 276, 127);
        flowers2 = crop(356, 639, 235, 113);
        flowers3 = crop(640, 627, 255, 136);
        flowers4 = crop(943, 634, 250, 125);

        bench = crop(56, 845, 295, 149);
        lamp = crop(449, 764, 76, 239);
        flowerBed = crop(586, 835, 319, 141);
    }

    private TextureRegion crop(
        int x, int y, int width, int height
    ) {
        return new TextureRegion(sheet, x, y, width, height);
    }

    @Override
    public void update(GameWorld world, float deltaTime) {

        batch.setProjectionMatrix(
            gameViewport.getCamera().combined
        );

        batch.begin();

        // =====================================
        // UPPER LEFT GARDEN
        // =====================================

        batch.draw(tree1, 364, 770, 115, 125);
        batch.draw(tree2, 500, 790, 120, 130);

        batch.draw(bush1, 105, 540, 65, 40);
        batch.draw(flowers1, 365, 550, 75, 35);

        // =====================================
        // UPPER MIDDLE GARDEN
        // =====================================

        batch.draw(bush2, 455, 550, 65, 40);
        batch.draw(flowers2, 535, 550, 65, 32);

        // =====================================
        // LOWER LEFT GARDEN
        // =====================================

        batch.draw(tree2, 405, 90, 110, 120);
        batch.draw(tree3, 820, 90, 105, 115);

        batch.draw(bush3, 105, 340, 65, 40);
        batch.draw(flowers3, 370, 340, 75, 35);

        // =====================================
        // LOWER MIDDLE GARDEN
        // =====================================

        batch.draw(tree1, 465, 100, 100, 110);

        batch.draw(bush4, 890, 340, 65, 40);
        batch.draw(flowers4, 1010, 340, 75, 35);

        // =====================================
        // BENCHES
        // Keep away from the lake and pathways
        // =====================================

        batch.draw(bench, 105, 550, 80, 40);
        batch.draw(bench, 1120, 520, 80, 40);

        batch.draw(bench, 105, 305, 80, 40);
        batch.draw(bench, 1120, 305, 80, 40);

        // =====================================
        // LAMPS
        // =====================================

        batch.draw(lamp, 465, 535, 20, 62);
        batch.draw(lamp, 850, 535, 20, 62);

        batch.draw(lamp, 465, 305, 20, 62);
        batch.draw(lamp, 850, 305, 20, 62);

        // =====================================
        // FLOWER BEDS
        // Positioned away from lower pathway
        // =====================================

        batch.draw(flowerBed, 105, 330, 75, 34);
        batch.draw(flowerBed, 1120, 330, 75, 34);

        // =====================================
        // TREES AROUND THE LAKE
        // =====================================

        batch.draw(tree4, 810, 790, 75, 85);
        batch.draw(tree4, 705, 475, 65, 75);
        batch.draw(tree4, 1090, 475, 65, 75);

        // =====================================
        // EXTRA CAMPUS FLOWERS
        // =====================================

        batch.draw(flowers1, 105, 730, 75, 35);
        batch.draw(flowers3, 430, 730, 75, 35);

        batch.draw(flowers2, 750, 770, 65, 32);
        batch.draw(flowers4, 1100, 490, 70, 35);

        batch.draw(flowers1, 105, 170, 75, 35);
        batch.draw(flowers3, 370, 180, 75, 35);

        batch.draw(flowers2, 890, 170, 75, 35);
        batch.draw(flowers4, 1120, 180, 75, 35);

        batch.end();
    }

    public void dispose() {
        batch.dispose();
        sheet.dispose();
    }
}
