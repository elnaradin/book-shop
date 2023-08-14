$(document).ready(function () {
    $("[data-sendstatus_unauthorized]").click(function (e) {
        //stops page from reloading
        e.preventDefault();
        const $this = $(this);
        const status = $this.attr('data-sendstatus_unauthorized');
        const data = {
            slug: $this.attr('data-bookid'),
            status: status
        };
        //sends request to add or remove slug from cookie
        $.ajax({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            type: 'POST',
            url: "/api/anonym/changeBookStatus",
            data: JSON.stringify(data),
            dataType: 'json'
        });
        //animation for unauthorized user
        if ($this.hasClass('cart_section')) {
            changeCartBlockBookAmountFromSections($this, $('.cartAmount'), $('.keptAmount'));
            removeCards($this);
            return;
        }
        if ($this.hasClass('kept_section')) {
            changeCartBlockBookAmountFromSections($this, $('.keptAmount'), $('.cartAmount'));
            removeCards($this);
            return;
        }
            changeCartBlockBookAmount($this, status);
    });

    $("[data-sendstatus]").click(function () {
        //animation for authorized user
        if ($(this).hasClass('cart_section')) {
            changeCartBlockBookAmountFromSections($(this), $('.cartAmount'), $('.keptAmount'));
            return;
        }
        if ($(this).hasClass('kept_section')) {
            changeCartBlockBookAmountFromSections($(this), $('.keptAmount'), $('.cartAmount'));
            return;
        }
        const status = $(this).attr('data-sendstatus');
        changeCartBlockBookAmount($(this), status);
    })
    function removeCards($this) {
        const $cart =  $this.closest('.Cart');
        let price = 0, priceOld = 0, booksId = [];
        $this.closest('.Cart-product').remove();
        $cart.find('.Cart-product').each(function () {
            let $this = $(this);
            price += parseFloat($this.find('.Cart-price:not(.Cart-price_old)').text().replace('₽', ''));
            priceOld += parseFloat($this.find('.Cart-price_old').text().replace('₽', ''));
            booksId.push($this.find('[data-sendstatus_unauthorized="CART"]').data('bookid'));
        });
        if(price === 0){
            $(".Cart-block_total").children().hide();
        }
        $cart.find('.Cart-total .Cart-price').not('.Cart-price_old').text(price + ' р.');
        $cart.find('.Cart-total .Cart-price_old').text(priceOld + ' р.');
        if ($cart.hasClass('Cart_postponed')) {
            $cart.find('.Cart-buyAll').data('bookid', booksId);
        }
        if (!$cart.find('.Cart-product').length) {
            if ($cart.hasClass('Cart_postponed')) {
                $cart.prepend('<div class="Cart-messageInfo">Отложенных книг нет</div>');
            } else {
                $cart.prepend('<div class="Cart-messageInfo">Корзина пуста</div>');
            }
            $cart.find('.Cart-total .btn').attr('disabled', 'disabled');
        }
    }

    function changeCartBlockBookAmountFromSections(btn, thisBlock, otherBlock) {
        const $this = btn;

        const block = thisBlock;
        const oppositeBlock = otherBlock;
        const count = parseInt(block.text());
        const oppositeCount = parseInt(oppositeBlock.text());
        if ($this.hasClass("btn_danger")) {
            block.text(count - 1);
        } else {
            oppositeBlock.text(oppositeCount + 1);
            block.text(count - 1);
        }
    }


    function changeCartBlockBookAmount(curBtn, status) {
        let cartBlock;
        let oppositeCartBlock;
        let oppositeBtn;
        let count;
        if (status === 'CART') {
            cartBlock = $(".cartAmount");
            oppositeCartBlock = $(".keptAmount");
            oppositeBtn = $(".keptBtn");
        }
        if (status === 'KEPT') {
            cartBlock = $(".keptAmount");
            oppositeCartBlock = $(".cartAmount");
            oppositeBtn = $(".cartBtn");
        }
        count = parseInt(cartBlock.text());
        if (curBtn.hasClass('btn_check')) {
            cartBlock.text(count - 1);
            curBtn.removeClass('btn_check');
        } else {
            cartBlock.text(count + 1);
        }
        if (oppositeBtn.hasClass('btn_check')) {
            const count2 = parseInt(oppositeCartBlock.text());
            oppositeCartBlock.text(count2 - 1);
            oppositeBtn.removeClass('btn_check');
        }
    }
});
