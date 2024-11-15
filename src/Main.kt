import java.util.*

/**
 *1. Приложение «Сеть магазинов по продаже телефонов».
 * 2. Необходимо написать приложение по продаже телефонов в двух разных городах.
 * 3. Оба магазина продают одинаковые модели телефонов, но цены в магазинах городов немного отличаются.
 * 4. При покупке телефона, приложение сообщает, что куплен тот или иной телефон.
 * 5. Также есть счетчики количества проданных телефонов каждой модели.
 * 6. Это интернет – магазин, поэтому он будет приветствовать,
 * предлагать право выбора города для покупки и выбор модели телефона для покупки.
 * 7. На ступени приветствия приложение будет предлагать выйти(закончить) работу,
 * на ступени выбора телефона приложение будет предлагать показ статистику покупок в этом магазине
 * (куплено телефонов такой – то модели – столько – то и т.д. Общая сумма покупок – такая-то).
 * 8. В одном из магазинов будет ремонтная мастерская, которая будет предлагать
 * починку имеющегося у Вас сломанного телефона.
 * Если да – телефон отремонтирован, если нет – не нуждаетесь в ремонте.
 * 9. Работа в приложении цикличная, т.е. после покупки, ремонта, оно снова Вам предложит выбор магазина,
 * выбор телефона для покупки.
 * Прекратить работу приложения можно на стартовой панели приветствия и выбора города.
 * 10. По – возможности ремонт телефона предлагать не более одного раза при посещении магазина.
 * 11. Данная работа предполагает использование абстрактных классов, интерфейсов, чтение с консоли,
 * вывод на консоль, цикличность кода, применение условных и арифметических операторов.
 */
fun main() {
    val stores = listOf(Moscow("Москва"), SaintPetersburg("Санкт-Петербург"))
    val repairShop = RepairShop()
    val scanner = Scanner(System.`in`)

    while (true) {
        println("Добро пожаловать в интернет-магазин телефонов!")
        println("Выберите город (1 - Москва, 2 - Санкт-Петербург, 0 - Выход):")
        when (scanner.nextInt()) {
            1 -> shopInStore(stores[0], repairShop)
            2 -> shopInStore(stores[1], repairShop)
            0 -> {
                println("Спасибо за использование приложения! До свидания!")
                return
            }
            else -> println("Неверный выбор, попробуйте снова.")
        }
    }
}

interface Phone {
    val model: String
    val price: Double
}

data class PhoneModel(override val model: String, override val price: Double) : Phone

abstract class Store(val city: String) {
    abstract val phones: List<Phone>
    abstract fun sellPhone(model: String)
    abstract fun showStatistics()
}

class Moscow(city: String) : Store(city) {
    private val soldPhonesCount = mutableMapOf<String, Int>()
    private var totalSales = 0.0

    override val phones = listOf(
        PhoneModel("iPhone 14", 999.99),
        PhoneModel("Samsung Galaxy S22", 799.99),
        PhoneModel("Google Pixel 6", 599.99)
    )

    override fun sellPhone(model: String) {
        val phone = phones.find { it.model == model }
        if (phone != null) {
            soldPhonesCount[model] = soldPhonesCount.getOrDefault(model, 0) + 1
            totalSales += phone.price
            println("Вы купили $model за ${phone.price} в магазине города $city.")
        } else {
            println("Телефон с моделью $model не найден.")
        }
    }

    override fun showStatistics() {
        println("Статистика продаж в магазине города $city:")
        soldPhonesCount.forEach { (model, count) ->
            println("Модель: $model, Продано: $count")
        }
        println("Общая сумма продаж: $totalSales")
    }
}

class SaintPetersburg(city: String) : Store(city) {
    private val soldPhonesCount = mutableMapOf<String, Int>()
    private var totalSales = 0.0

    override val phones = listOf(
        PhoneModel("iPhone 14", 995.00),
        PhoneModel("Samsung Galaxy S22", 795.00),
        PhoneModel("Google Pixel 6", 595.00)
    )

    override fun sellPhone(model: String) {
        val phone = phones.find { it.model == model }
        if (phone != null) {
            soldPhonesCount[model] = soldPhonesCount.getOrDefault(model, 0) + 1
            totalSales += phone.price
            println("Вы купили $model за ${phone.price} в магазине города $city.")
        } else {
            println("Телефон с моделью $model не найден.")
        }
    }

    override fun showStatistics() {
        println("Статистика продаж в магазине города $city:")
        soldPhonesCount.forEach { (model, count) ->
            println("Модель: $model, Продано: $count")
        }
        println("Общая сумма продаж: $totalSales")
    }
}

class RepairShop {
    fun offerRepair(): Boolean {
        println("У вас есть сломанный телефон? (да/нет)")
        val response = readlnOrNull()
        return response?.lowercase(Locale.getDefault()) == "да"
    }

    fun repairPhone() {
        println("Ваш телефон отремонтирован!")
    }
}



fun shopInStore(store: Store, repairShop: RepairShop) {
    var repairOffered = false

    while (true) {
        println("Вы находитесь в магазине города ${store.city}.")
        println("Доступные телефоны:")
        store.phones.forEach { println("${store.phones.indexOf(it) + 1}.${it.model} - ${it.price}") }
        println("Выберите модель телефона для покупки или введите 'статистика' для просмотра статистики (или 'выход' для выхода):")

        val input = readlnOrNull()
        when {
            input.equals("выход", ignoreCase = true) -> return
            input.equals("статистика", ignoreCase = true) -> store.showStatistics()
            store.phones.any { (store.phones.indexOf(it) + 1).toString() == input } -> {
                store.sellPhone(store.phones[input?.toInt()?.minus(1)!!].model)
                if (store.city == "Москва"){
                    if (!repairOffered) {
                        if (repairShop.offerRepair()) {
                            repairShop.repairPhone()
                            repairOffered = true
                        }
                    }
                }
            }
            else -> println("Неверный выбор, попробуйте снова.")
        }
    }
}