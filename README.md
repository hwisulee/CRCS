# CRCS
>기업연계프로젝트인 캡스톤 디자인 팀 프로젝트에 의해 제작한 CRCS(CropRemoteControlSystem) 어플리케이션 (Android Only)

<pre>
<code>
주제 : 농작물 원격 제어 시스템</br>
설명 : 아두이노와 안드로이드 어플리케이션을 사용한 농작물 원격 제어 시스템 구축</br>
팀 인원 : 4명</br>
맡은 역할 : 앱 개발 및 아두이노와 Google firebase 연동을 통한 데이터 송수신</br>
개발 기간 : '22.03.08 ~ 개발 중</br>
개발 언어 : Java</br>
개발 환경 : Windows 10 pro,
            Jdk 1.8('22.03.08 ~ '22.05.14) -> Jdk 11('22.05.15),
            Android Studio Bumblebee('22.03.08 ~ '22.05.14) -> Android Studio Chipmunk('22.05.15)
</code>
</pre>

## Table of Contents
1. [Preview](#preview)
2. [Library](#library)
3. [License](#license)

<h2 id="preview">Preview</h2>

>'22.05.14 진행상황 '현재 상태 : 미완성'

1. 실행시 기본적인 동작화면

![crcs1](https://user-images.githubusercontent.com/62528282/168420713-c81772cd-8035-4db8-9f9c-126adffc2a25.gif)

2. 유저 위치 설정 프로세스

![crcs2](https://user-images.githubusercontent.com/62528282/168420715-13924e93-3e7f-4046-9a61-73eb41716054.gif)

3. firebase Realtime Database 연동 및 컨트롤(gif 속도 맞추는 과정에서 오차가 생긴 것, 실제로는 지연시간 없음)

![ezgif com-gif-maker](https://user-images.githubusercontent.com/62528282/172094512-967550b6-a4e3-4208-9e1a-7ea8509f8f16.gif)
![db](https://user-images.githubusercontent.com/62528282/172094510-7fc223a0-2500-4805-bb15-63d09cb5daed.gif)

<h2 id="library">Library</h2>

>This Project included this Library and API.

1. [기상청_단기예보_조회서비스](https://www.data.go.kr/tcs/dss/selectApiDataDetailView.do?publicDataPk=15084084)
2. [Kakao Maps API](https://apis.map.kakao.com)
3. [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart)
4. [Android-SpinKit](https://github.com/ybq/Android-SpinKit)
5. [ArcSeekBar](https://github.com/MarcinMoskala/ArcSeekBar)
6. [RoomDB](https://developer.android.com/training/data-storage/room?hl=ko)
7. [APACHE POI, POI-OOXML](https://poi.apache.org/)
8. [Google firebase](https://firebase.google.com/?hl=ko)
9. [Google OssLicenses plugin](https://developers.google.com/android/guides/opensource)

<h2 id="license">License</h2>

>Library and API has this Licenses

1. 기상청_단기예보_조회서비스

공공저작물_출처표시

![data-mark01-opencode1-m](https://user-images.githubusercontent.com/62528282/168416204-7146cc0f-b21b-4367-9a8f-3787d1ac7a5e.png)

[More Information1](http://ccl.cckorea.org/about/)

[More Information2](https://www.kogl.or.kr/info/license.do)

</br>

2. Kakao Maps API

OSS Notice | KakaoSDK2-Android
This application is Copyright © Kakao Corp. All rights reserved.

The following sets forth attribution notices for third party software that may be contained in this application.

[More Information](http://t1.daumcdn.net/osa/notice/173/1jnBpKCehN/notice.html)

</br>

3. MPAndroidChart

Copyright 2020 Philipp Jahoda

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

[More Information](https://github.com/PhilJay/MPAndroidChart/blob/master/LICENSE)

</br>

4. Android-SpinKit

The MIT License (MIT)

Copyright © 2016 ybq

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

[More Information](https://github.com/ybq/Android-SpinKit/blob/master/LICENSE)

</br>

5. ArcSeekBar

Copyright 2017 Marcin Moskała

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

[More Information](https://github.com/MarcinMoskala/ArcSeekBar/blob/master/LICENSE)

</br>

6. RoomDB

Version 2.0, January 2004

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

[More Information](https://developer.android.com/license?hl=ko)

</br>

7. APACHE POI, POI-OOXML

Version 2.0, January 2004

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

APACHE POI SUBCOMPONENTS:

Apache POI includes subcomponents with separate copyright notices and
license terms. Your use of these subcomponents is subject to the terms
and conditions of the following licenses:

[More Information](https://github.com/apache/poi/blob/trunk/legal/LICENSE)

</br>

8. Google firebase

Version 2.0, January 2004

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

[More Information](https://github.com/firebase/quickstart-android/blob/master/LICENSE)

</br>

9. Google OssLicenses plugin

Version 2.0, January 2004

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

[More Information1](https://creativecommons.org/licenses/by/4.0/)
[More Information2](https://www.apache.org/licenses/LICENSE-2.0)

</br>

>My Project has this License

MIT License

Copyright (c) 2022 hwisulee

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
