using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Capstone.Models.Entities.Services
{
    public partial interface ICarParkService
    {
        int GetEmptyAmount(int carParkId);
        IQueryable<CarParkAmount> GetCoordinatesWithEmptyAmount();
    }

    public partial class CarParkService
    {
        /// <summary>
        /// Lấy số lượng chỗ còn trống trong bãi giữ xe
        /// </summary>
        /// <param name="carParkId"></param>
        /// <returns>
        /// int (-1 khi carPark đã bị Deactive hoặc không tìm thấy)
        /// </returns>
        public int GetEmptyAmount(int carParkId)
        {
            var carPark = this.Get(carParkId);
            if(carPark!= null && carPark.Active != false)
            {
                return carPark.Areas.Sum(q => q.EmptyAmount);
            }
            return -1;
        }

        public IQueryable<CarParkAmount> GetCoordinatesWithEmptyAmount()
        {
            var carPark = this.GetActive().Select(q => new CarParkAmount()
            {
                CarPark = q,
                EmptyAmount = q.Areas.Sum(x => x.EmptyAmount),
            });
            return carPark;
        }

    }

    public class CarParkAmount
    {
        public CarPark CarPark { get; set; }
        public int EmptyAmount { get; set; }
    }
}