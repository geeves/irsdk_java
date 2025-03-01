/*
 *
 *    Copyright (C) 2020 Joffrey Bonifay
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package com.joffrey.iracing.irsdkjava;

import com.joffrey.iracing.irsdkjava.config.FluxProperties;
import com.joffrey.iracing.irsdkjava.model.Header;
import com.joffrey.iracing.irsdkjava.model.SdkStarter;
import com.joffrey.iracing.irsdkjava.trackmaptracker.TrackmapTrackerService;
import com.joffrey.iracing.irsdkjava.yaml.YamlService;
import com.joffrey.iracing.irsdkjava.yaml.irsdkyaml.YamlFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;
import lombok.extern.java.Log;

import java.nio.ByteBuffer;

import static com.joffrey.iracing.irsdkjava.YamlHelperTest.createByteBufferYamlFile;
import static com.joffrey.iracing.irsdkjava.YamlHelperTest.loadYamlObject;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@Log
@ExtendWith(SpringExtension.class)
class TestTrackmapTrackerDriverService {

	@MockitoBean
	private SdkStarter sdkStarter;
	@MockitoBean
	private Header header;
	@MockitoBean
	private YamlService yamlService;

	// Class under test
	private TrackmapTrackerService trackmapTrackerService;

	// Helpers Objects
	private ByteBuffer byteBufferYamlFile;


	@BeforeEach
	void init() throws Exception {
		trackmapTrackerService = new TrackmapTrackerService(new FluxProperties(), sdkStarter, yamlService);
		byteBufferYamlFile = createByteBufferYamlFile("trackmaptracker/trackmaptracker.yml");
		YamlFile yamlFile = loadYamlObject(byteBufferYamlFile);

		Mockito.when(sdkStarter.getHeader()).thenReturn(header);
		Mockito.when(sdkStarter.getHeader().getSessionInfoByteBuffer()).thenReturn(byteBufferYamlFile);
		Mockito.when(sdkStarter.isRunning()).thenReturn(true);
		Mockito.when(yamlService.getYamlFile()).thenReturn(yamlFile);
	}

	@DisplayName("getTrackmapTrackerListFlux() - Test if data where good fetched from yaml and fake MemMapFile")
	@Test
	void Given_ValidDataFromYamlAndMemMapFile_When_CallingFlux_Then_SHouldReturnGoodDataInFlux() {

		doReturn(0.0f).when(sdkStarter).getVarFloat("CarIdxLapDistPct", 63);
		doReturn(0.0f).when(sdkStarter).getVarFloat("CarIdxLapDistPct", 12);
		doReturn(0.0f).when(sdkStarter).getVarFloat("CarIdxLapDistPct", 23);
		doReturn(0.0f).when(sdkStarter).getVarFloat("CarIdxLapDistPct", 49);

		StepVerifier.create(trackmapTrackerService.getTrackmapTrackerListFlux()).assertNext(trackmapTrackers -> {
			assertThat(trackmapTrackers.get(0).getDriverIdx()).isEqualTo(63);
			assertThat(trackmapTrackers.get(0).getDriverCarNbr()).isEqualTo(63);
			assertThat(trackmapTrackers.get(0).getDriverInitials()).isEqualTo("JO");
			assertThat(trackmapTrackers.get(0).getDriverDistPct()).isEqualTo(0.0f);

			assertThat(trackmapTrackers.get(1).getDriverIdx()).isEqualTo(12);
			assertThat(trackmapTrackers.get(1).getDriverCarNbr()).isEqualTo(12);
			assertThat(trackmapTrackers.get(1).getDriverInitials()).isEqualTo("DT");
			assertThat(trackmapTrackers.get(1).getDriverDistPct()).isEqualTo(0.0f);

			assertThat(trackmapTrackers.get(2).getDriverIdx()).isEqualTo(23);
			assertThat(trackmapTrackers.get(2).getDriverCarNbr()).isEqualTo(23);
			assertThat(trackmapTrackers.get(2).getDriverInitials()).isEqualTo("DT");
			assertThat(trackmapTrackers.get(2).getDriverDistPct()).isEqualTo(0.0f);

			assertThat(trackmapTrackers.get(3).getDriverIdx()).isEqualTo(49);
			assertThat(trackmapTrackers.get(3).getDriverCarNbr()).isEqualTo(49);
			assertThat(trackmapTrackers.get(3).getDriverInitials()).isEqualTo("DF");
			assertThat(trackmapTrackers.get(3).getDriverDistPct()).isEqualTo(0.0f);

		}).thenCancel().verifyThenAssertThat().hasNotDroppedElements().hasNotDroppedErrors().hasNotDiscardedElements();

	}

	@DisplayName("getTrackmapTrackerListFlux() new values - Test if data where good fetched from yaml and fake MemMapFile")
	@Test
	void Given_OthersValidDataFromYamlAndMemMapFile_When_CallingFlux_Then_SHouldReturnGoodDataInFlux() {

		doReturn(10.0f).when(sdkStarter).getVarFloat("CarIdxLapDistPct", 63);
		doReturn(20.0f).when(sdkStarter).getVarFloat("CarIdxLapDistPct", 12);
		doReturn(30.0f).when(sdkStarter).getVarFloat("CarIdxLapDistPct", 23);
		doReturn(40.0f).when(sdkStarter).getVarFloat("CarIdxLapDistPct", 49);

		StepVerifier.create(trackmapTrackerService.getTrackmapTrackerListFlux()).assertNext(trackmapTrackers -> {

			assertThat(trackmapTrackers.get(0).getDriverIdx()).isEqualTo(63);
			assertThat(trackmapTrackers.get(0).getDriverCarNbr()).isEqualTo(63);
			assertThat(trackmapTrackers.get(0).getDriverInitials()).isEqualTo("JO");
			assertThat(trackmapTrackers.get(0).getDriverDistPct()).isEqualTo(10.0f);

			assertThat(trackmapTrackers.get(1).getDriverIdx()).isEqualTo(12);
			assertThat(trackmapTrackers.get(1).getDriverCarNbr()).isEqualTo(12);
			assertThat(trackmapTrackers.get(1).getDriverInitials()).isEqualTo("DT");
			assertThat(trackmapTrackers.get(1).getDriverDistPct()).isEqualTo(20.0f);

			assertThat(trackmapTrackers.get(2).getDriverIdx()).isEqualTo(23);
			assertThat(trackmapTrackers.get(2).getDriverCarNbr()).isEqualTo(23);
			assertThat(trackmapTrackers.get(2).getDriverInitials()).isEqualTo("DT");
			assertThat(trackmapTrackers.get(2).getDriverDistPct()).isEqualTo(30.0f);

			assertThat(trackmapTrackers.get(3).getDriverIdx()).isEqualTo(49);
			assertThat(trackmapTrackers.get(3).getDriverCarNbr()).isEqualTo(49);
			assertThat(trackmapTrackers.get(3).getDriverInitials()).isEqualTo("DF");
			assertThat(trackmapTrackers.get(3).getDriverDistPct()).isEqualTo(40.0f);

		}).thenCancel().verifyThenAssertThat().hasNotDroppedElements().hasNotDroppedErrors().hasNotDiscardedElements();


	}

}
