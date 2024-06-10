document.addEventListener('DOMContentLoaded', () => {
    const audioPlayer = document.getElementById('audioPlayer');
    const playButtons = document.querySelectorAll('.playButton')
    let currentIndex = 0
    const nextButton = document.getElementById('nextButton')
    const prevButton = document.getElementById('prevButton')
    const trackSinger = document.getElementById('trackText')

    nextButton.onclick = () => {
        currentIndex = (currentIndex + 1) % playButtons.length
        trackSinger.innerText = updateTrackText(currentIndex)
        loadAudio(currentIndex)
    }

    prevButton.onclick = () => {
        currentIndex = (currentIndex - 1 + playButtons.length) % playButtons.length
        trackSinger.innerText = updateTrackText(currentIndex)
        loadAudio(currentIndex)
    }

    // Устанавливаем обработчик события клика для всех кнопок Play
    playButtons.forEach((button, index) => {
        button.onclick= () => {
            currentIndex = index
            loadAudio(currentIndex)
        }
    })

    // Устанавливаем обработчик события окончания воспроизведения
    audioPlayer.onended = () => {
        currentIndex = (currentIndex + 1) % playButtons.length
        loadAudio(currentIndex)
    }

    // Функция для загрузки аудиофайла
    function loadAudio(index) {
        const audioSrc = playButtons[index]?.parentNode?.getAttribute('data-src');
        if (audioSrc) {
            const link = 'songs/music/' + audioSrc
            trackSinger.innerText = updateTrackText(index)
            audioPlayer.src = link
            audioPlayer.load()
            audioPlayer.play()
        }
    }

    function updateTrackText(index) {
        const table = document.getElementById('audioTable')
        const rows = table.getElementsByTagName('tr')

        if (index >= 0 && index < rows.length) {
            const cells = rows[index + 1].getElementsByTagName('td')

            if (cells.length >= 3) {
                // Обновляем значение <span> с новой информацией о треке
                return cells[0].innerText + ' - ' + cells[1].innerText
            }
        }
    }
})