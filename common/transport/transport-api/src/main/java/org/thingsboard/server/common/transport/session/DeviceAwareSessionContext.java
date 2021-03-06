/**
 * Copyright © 2016-2020 The Thingsboard Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.thingsboard.server.common.transport.session;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.thingsboard.server.common.data.DeviceProfile;
import org.thingsboard.server.common.data.id.DeviceId;
import org.thingsboard.server.common.msg.session.SessionContext;
import org.thingsboard.server.common.transport.auth.TransportDeviceInfo;
import org.thingsboard.server.gen.transport.TransportProtos;
import org.thingsboard.server.gen.transport.TransportProtos.DeviceInfoProto;

import java.util.UUID;

/**
 * @author Andrew Shvayka
 */
@Data
public abstract class DeviceAwareSessionContext implements SessionContext {

    @Getter
    protected final UUID sessionId;
    @Getter
    private volatile DeviceId deviceId;
    @Getter
    protected volatile TransportDeviceInfo deviceInfo;
    @Getter
    @Setter
    protected volatile DeviceProfile deviceProfile;
    @Getter
    @Setter
    private volatile TransportProtos.SessionInfoProto sessionInfo;

    private volatile boolean connected;

    public DeviceId getDeviceId() {
        return deviceId;
    }

    public void setDeviceInfo(TransportDeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
        this.connected = true;
        this.deviceId = deviceInfo.getDeviceId();
    }

    @Override
    public void onProfileUpdate(DeviceProfile deviceProfile) {
        this.deviceProfile = deviceProfile;
        this.deviceInfo.setDeviceType(deviceProfile.getName());
        this.sessionInfo = TransportProtos.SessionInfoProto.newBuilder().mergeFrom(sessionInfo).setDeviceType(deviceProfile.getName()).build();
    }

    public boolean isConnected() {
        return connected;
    }

    public void setDisconnected() {
        this.connected = false;
    }
}
