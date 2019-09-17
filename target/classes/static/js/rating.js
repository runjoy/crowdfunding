var flag = true;

document.getElementById('company-text-field').innerHTML =
    marked($("#company-text").val());

var cStars = function(nowPos) {
    if (flag) {
        $('.stars .star').removeClass('active');
        for (var i = 0; nowPos + 1 > i; i++) {
            $('.stars .star').eq(i).toggleClass('active');
        }
    }
};

var starsCount = $('.star.active').length;

$('.stars .star').hover(function() {
    if (flag) {
        for (var i = 0; $(this).index() + 1 > i; i++) {
            $('.stars .star').eq(i).toggleClass('hover_star');
        }
    }
});

$('.stars .star').click(function() {
    if (flag) {
        for (var i = 0; $(this).index() + 1 > i; i++) {
            $('.stars .star').eq(i).toggleClass('hover_star');
            $('.stars .star').eq(i).toggleClass('hover_star');
        }
        starsCount = $('.star.hover_star').length;
        flag = false;
    }
});

$('.stars .star').on('mouseleave', function() {
    cStars(+starsCount - 1);
});

var app = angular.module("VoteManagement", []);
app.controller("VoteController", function($scope, $http) {

    $scope.companyRatingForm = {
        companyId: 0,
        vote: 0
    };

    $scope.addVote = function (vote) {
        $scope.companyRatingForm.companyId = $scope.company_id;
        console.log($scope.company_id);
        $scope.companyRatingForm.vote = vote;
        $http({
            method: "POST",
            url: '/vote',
            data: angular.toJson($scope.companyRatingForm),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(_success, _error);
    }

    function _success(res) {
        if ($.isNumeric(res.data)) {
            $scope.rating = res.data;
        }
        console.log("success");
    }

    function _error() {
        console.log("_error");
    }
});