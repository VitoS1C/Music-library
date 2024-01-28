document.addEventListener('DOMContentLoaded', function () {
    const audioPlayer = document.getElementById('audioPlayer');
    const playButtons = document.querySelectorAll('.playButton');
    const playDiv = document.querySelectorAll('.border-4')
    let currentIndex = 0;
    const nextButton = document.getElementById('nextButton');
    const prevButton = document.getElementById('prevButton');
    const trackSinger = document.getElementById('trackText');

    nextButton.addEventListener('click', nextTrack);
    prevButton.addEventListener('click', prevTrack);


    // Устанавливаем обработчик события клика для всех кнопок Play
    playButtons.forEach(function (button, index) {
        button.addEventListener('click', function () {
            currentIndex = index;
            loadAudio(currentIndex);
        });
    });

    playDiv.forEach(function (div, index) {
        div.addEventListener('click', function () {
            currentIndex = index;
            loadAudio(currentIndex);
        })
    })

    // Устанавливаем обработчик события окончания воспроизведения
    audioPlayer.addEventListener('ended', function () {
        currentIndex = (currentIndex + 1) % playButtons.length;
        loadAudio(currentIndex);
    });

    // Функция для загрузки аудиофайла
    function loadAudio(index) {
        const audioSrc = playButtons[index]?.parentNode?.getAttribute('data-src');
        if (audioSrc) {
            trackSinger.innerText = updateTrackText(index);
            audioPlayer.src = audioSrc;
            audioPlayer.load();
            audioPlayer.play();
        }
    }

    function nextTrack() {
        currentIndex = (currentIndex + 1) % playButtons.length;
        trackSinger.innerText = updateTrackText(currentIndex);
        loadAudio(currentIndex);
    }

    function prevTrack() {
        currentIndex = (currentIndex - 1 + playButtons.length) % playButtons.length;
        trackSinger.innerText = updateTrackText(currentIndex);
        loadAudio(currentIndex);
    }

    function updateTrackText(index) {
        var table = document.getElementById('audioTable');
        var rows = table.getElementsByTagName('tr');

        if (index >= 0 && index < rows.length) {
            const cells = rows[index + 1].getElementsByTagName('td');

            if (cells.length >= 3) {
                // Обновляем значение <span> с новой информацией о треке
                return cells[0].innerText + ' - ' + cells[1].innerText;
            }
        }
    }
});