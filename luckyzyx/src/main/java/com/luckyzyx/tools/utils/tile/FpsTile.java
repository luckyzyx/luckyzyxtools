package com.luckyzyx.tools.utils.tile;

import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import com.joom.paranoid.Obfuscate;
import com.luckyzyx.tools.utils.ShellUtils;

@Obfuscate
public class FpsTile extends TileService {
    @Override
    public void onTileAdded() {
        super.onTileAdded();
    }

    @Override
    public void onTileRemoved() {
        super.onTileRemoved();
    }

    @Override
    public void onClick() {
        super.onClick();
        Tile tile = getQsTile();
        switch (tile.getState()){
            //不可用状态
            case Tile.STATE_UNAVAILABLE:
                break;
            //禁用状态
            case Tile.STATE_INACTIVE:
                ShellUtils.execCommand("su -c service call SurfaceFlinger 1035 i32 1",true);
                tile.setState(Tile.STATE_ACTIVE);
                tile.updateTile();
                break;
            //开启状态
            case Tile.STATE_ACTIVE:
                ShellUtils.execCommand("su -c service call SurfaceFlinger 1035 i32 -1",true);
                tile.setState(Tile.STATE_INACTIVE);
                tile.updateTile();
                break;
            default:
                break;
        }
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
    }

    @Override
    public void onStopListening() {
        super.onStopListening();
    }
}
